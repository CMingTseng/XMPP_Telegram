package XMPP_Telegram.model;

import XMPP_Telegram.controller.XMPPController;
import XMPP_Telegram.util.BotUtil;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.java7.XmppHostnameVerifier;
import org.jivesoftware.smack.packet.EmptyResultIQ;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import javax.net.ssl.*;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class XMPPConnection {
    private AbstractXMPPConnection connection = null;
    private XMPPTCPConnectionConfiguration configuration = null;
    private String server;
    private String login;
    private String password;
    private int port;
    private ChatManager chatManager;
    private IncomingChatMessageListener listener;
    private XMPPController controller;
    private SSLSetting sslSetting = new SSLSetting();

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
                .setDebuggerEnabled(true)
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
     *Создание подключения
     */
    public void createConnection() throws Exception {
        try {
            configure();
            connect();
            login();
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(listener = new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    if (message.getType().equals(Message.Type.chat) && message.getBody() != null) {
                        try {
                            controller.receivedMessage(BotUtil.getXMPPServer(message.getTo().toString()), BotUtil.getXMPPLogin(message.getTo().toString()), BotUtil.getXMPPLogin(message.getFrom().toString()), message.getBody());
                            IQ iq = new EmptyResultIQ();
                            iq.setType(IQ.Type.result);
                            iq.setStanzaId(message.getStanzaId());
                            iq.setTo(from);
                            connection.sendStanza(iq);
                        } catch (SmackException.NotConnectedException | InterruptedException e) {
                            //TODO
                            e.printStackTrace();
                        }
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
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public boolean equalsByXMPPAccount(XMPPAccount account) {
        return login.equals(account.getLogin()) && server.equals(account.getServer());
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void setController(XMPPController controller) {
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

        protected SSLContext getSslContext() {
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

        protected HostnameVerifier getHostNameVerifier() {
            //Создаем настройки для проверки хоста
            return new XmppHostnameVerifier();
        }
    }
}
