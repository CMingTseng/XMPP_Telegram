package XMPP_Telegram.model;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.java7.XmppHostnameVerifier;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
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
    private XMPPAccount xmppAccount;
    private SSLSetting sslSetting = new SSLSetting();

    public XMPPConnection(XMPPAccount xmppAccount) {
        this.xmppAccount = xmppAccount;
    }

    private void configure() throws Exception { //собираем настройки и проверяем их на корректность. Пробрасываем исключение, если возникнет
        configuration = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.domainBareFrom(xmppAccount.getServer()))
                .setPort(xmppAccount.getPort())
                .setHost(xmppAccount.getServer())
                .setHostAddress(InetAddress.getByName(xmppAccount.getServer()))
                .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
                .setCustomSSLContext(sslSetting.getSslContext()) //неизвестно, как это поведет себя если подключение отличается от заданного эталона - TLS
                .setDebuggerEnabled(true)
                .setHostnameVerifier(sslSetting.getHostNameVerifier())
                .build();
    }

    private void connect() throws Exception { //создаем подключение к серверу XMPP. Если соединение не удалось - пробрысываем исключение
        if (configuration != null) {
            connection = new XMPPTCPConnection(configuration);
        }else {
            configure();
            connection = new XMPPTCPConnection(configuration);
        }
        connection.connect();
    }

    private void login() throws Exception {
        connection.login(xmppAccount.getLogin(), xmppAccount.getPassword()); //если не получилось авторизоваться, пробрасываем исключение
    }

    /*
     *Создание подключения
     */
    public void createConnection() throws Exception {
        try {
            configure();
            connect();
            login();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void close() {
        connection.disconnect();
        Thread.currentThread().interrupt();
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public XMPPAccount getXmppAccount() {
        return xmppAccount;
    }

    private class SSLSetting {
        //делаем доверие для всех входящих сертификатов
        //TODO предусмотреть возможность выбора сертификатов
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                //System.out.println("getAcceptedIssuers");
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //System.out.println("Сведения о сертификате : " + chain[0].getIssuerX500Principal().getName() + "\n Тип авторизации : " + authType);
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //System.out.println("checkClientTrusted : " + authType);
            }
        }};
        protected SSLContext getSslContext()  {
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
//            return (s, sslSession) -> s.equals(sslSession.getPeerHost());
        }
    }
}
