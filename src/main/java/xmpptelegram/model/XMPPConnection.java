package xmpptelegram.model;

import xmpptelegram.bot.XMPPBot;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.java7.XmppHostnameVerifier;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class XMPPConnection implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMPPConnection.class);

    private AbstractXMPPConnection connection = null;
    private XMPPTCPConnectionConfiguration configuration = null;
    private String server;
    private String login;
    private String password;
    private int port;
    private ChatManager chatManager;
    private IncomingChatMessageListener listener;
    private XMPPBot controller;
    private SSLSetting sslSetting = new SSLSetting();
    private Map<String, Chat> map;

    public XMPPConnection(XMPPAccount xmppAccount) {
        server = xmppAccount.getServer();
        login = xmppAccount.getLogin();
        password = xmppAccount.getPassword();
        port = xmppAccount.getPort();
    }

    private void configure() throws Exception { //собираем настройки и проверяем их на корректность. Пробрасываем исключение, если возникнет
        configuration = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.domainBareFrom(server))
                .setPort(port)
                .setHost(server)
                .setHostAddress(InetAddress.getByName(server))
                .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
                .setCustomSSLContext(sslSetting.getSslContext()) //неизвестно, как это поведет себя если подключение отличается от заданного эталона - TLS
//                .setDebuggerEnabled(true)
                .setHostnameVerifier(sslSetting.getHostNameVerifier())
                .build();
    }

    private void connect() throws Exception { //создаем подключение к серверу XMPP. Если соединение не удалось - пробрысываем исключение
        if (configuration != null) {
            connection = new XMPPTCPConnection(configuration);
        } else {
            configure();
            connection = new XMPPTCPConnection(configuration);
        }
        connection.connect();
    }

    private void login() throws Exception {
        connection.login(login, password); //если не получилось авторизоваться, пробрасываем исключение
    }

    /*
     *Отправляет текущий статус учетной записи в Telegram
     */
    private void sendCheckStatus() {
        String status;
        if (isConnected())
            status = "Подключен";
        else
            status = "Не в сети";
        controller.receiveXMPPMessage(server, login, "status message", status);
    }

    /*
     *Создание подключения
     */
    private void createConnection() throws Exception {
        try {
            configure();
            connect();
            login();
            sendCheckStatus();
            map = new HashMap<String, Chat>();
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(listener = new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    if (message.getType().equals(Message.Type.chat) && message.getBody() != null) {
//                        try {
                        map.put(message.getFrom().asEntityBareJidIfPossible().toString(), chat);
                        controller.receiveXMPPMessage(server, login, message.getFrom().asEntityBareJidIfPossible().toString(), message.getBody());
                    }
                }
            });
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        if (listener != null)
            chatManager.removeListener(listener);
        if (connection.isConnected())
            connection.disconnect();
        sendCheckStatus();
        Thread.currentThread().interrupt();
    }

    public void sendMessage(TransferMessage transferMessage) {
        Message message = new Message();
        message.setType(Message.Type.chat);
        message.setBody(transferMessage.getText());
        try {
            if (!map.containsKey(transferMessage.getChatMap().getXmppContact())) {
                EntityBareJid to = JidCreate.entityBareFrom(transferMessage.getChatMap().getXmppContact());
                map.put(transferMessage.getChatMap().getXmppContact(), chatManager.chatWith(to));
            }
            map.get(transferMessage.getChatMap().getXmppContact()).send(message);
        } catch (SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
            LOGGER.warn("Can't send message to XMPP! " + message.toString());
        }
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean equalsByXMPPAccount(XMPPAccount account) {
        return login.equals(account.getLogin()) && server.equals(account.getServer());
    }

    @Override
    public synchronized void run() {
        try {
            createConnection();
        } catch (Exception e) {
            LOGGER.error(String.format("Error connection XMPPAccount. Login: %s, server: %s", login, server), e);
        }
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void setController(XMPPBot controller) {
        this.controller = controller;
    }

    private class SSLSetting {
        //делаем доверие для всех входящих сертификатов
        //TODO предусмотреть возможность выбора сертификатов
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        SSLContext getSslContext() {
            //создаем настройки для работы с TLS
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new SecureRandom());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                return null;
            }
            return sslContext;
        }

        HostnameVerifier getHostNameVerifier() {
            //Создаем настройки для проверки хоста
            return new XmppHostnameVerifier();
        }
    }
}
