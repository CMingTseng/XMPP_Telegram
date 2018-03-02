package XMPP_Telegram.model;


import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.annotation.PreDestroy;

public class XMPPAccount{

    private String server;

    private String login;

    private String password;

    private int port;

    private XMPPConnection connection;

    private ChatManager chatManager;

    public XMPPAccount() {
        connection = new XMPPConnection(this);
    }

    public XMPPAccount(String server, String login, String password) {
        this.server = server;
        this.login = login;
        this.password = password;
        port = 5222;
        connection = new XMPPConnection(this);
    }

    public XMPPAccount(String server, String login, String password, int port) {
        this.server = server;
        this.login = login;
        this.password = password;
        this.port = port;
        connection = new XMPPConnection(this);
    }

    public void Connect() {
        try {
            connection.createConnection();
            chatManager = ChatManager.getInstanceFor(connection.getConnection());
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    System.out.println(message.getBody());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void Disconnect() {
        connection.close();
    }


    public boolean isActive() {
        return connection.getConnection().isConnected();
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "XMPPAccount{" +
                "server='" + server + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                '}';
    }
}
