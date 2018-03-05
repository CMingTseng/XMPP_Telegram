package XMPP_Telegram.model;


import XMPP_Telegram.controller.XMPPController;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.Date;

public class XMPPAccount{

    private int id;

    private String server;

    private String login;

    private String password;

    private int port;

    private boolean saveHistory;

    private XMPPConnection connection;

    private ChatManager chatManager;

    @Autowired
    private XMPPController controller;

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

    public void connect() {
        try {
            connection.createConnection();
            chatManager = ChatManager.getInstanceFor(connection.getConnection());
            System.out.println(toString());
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    TransferMessage transferMessage = new TransferMessage();
                    transferMessage.setAccount(XMPPAccount.this);
                    transferMessage.setDate(new Date());
                    transferMessage.setContact(from.asEntityBareJid().toString());
                    transferMessage.setFromXMPP(true);
                    transferMessage.setText(message.getBody());
                    transferMessage.setSent(false);
                    controller.saveMessage(transferMessage);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void disconnect() {
        connection.close();
    }


    protected boolean isActive() {
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

    public void setController(XMPPController controller) {
        this.controller = controller;
    }

    public boolean isSaveHistory() {
        return saveHistory;
    }

    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "XMPPAccount{" +
                "server='" + server + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", saveHistory=" + saveHistory +
                '}';
    }
}
