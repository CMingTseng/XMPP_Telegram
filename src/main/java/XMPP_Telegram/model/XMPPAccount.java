package XMPP_Telegram.model;


import XMPP_Telegram.controller.XMPPController;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.EmptyResultIQ;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;


public class XMPPAccount {

    private int id;

    private String server;

    private String login;

    private String password;

    private int port;

    private boolean saveHistory;

    private boolean active;

    private XMPPConnection connection;

    private ChatManager chatManager;

    private XMPPController controller;

    private TelegramUser telegramUser;

    private Map<String, Long> map;

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
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    if (message.getType().equals(Message.Type.chat)) {
                        controller.saveMessage(new TransferMessage(message, XMPPAccount.this));
                        try {
                            IQ iq = new EmptyResultIQ();
                            iq.setType(IQ.Type.result);
                            iq.setStanzaId(message.getStanzaId());
                            iq.setTo(from);
                            connection.getConnection().sendStanza(iq);
                        } catch (SmackException.NotConnectedException | InterruptedException e) {
                            //TODO
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void disconnect() {
        connection.close();
    }

    public boolean isAlive() {
        return connection.getConnection().isConnected();
    }

    public boolean isActive() {
        return active;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, Long> getMap() {
        return map;
    }

    public void setMap(Map<String, Long> map) {
        this.map = map;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
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
