package xmpptelegram.bot;

import xmpptelegram.model.TransferMessage;
import xmpptelegram.model.XMPPAccount;
import xmpptelegram.model.XMPPConnection;
import xmpptelegram.service.MessageService;
import xmpptelegram.service.XMPPAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class XMPPBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMPPBot.class);

    private List<XMPPConnection> connections;

    private XMPPAccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public XMPPBot(XMPPAccountService accountService) {
        this.accountService = accountService;
    }

    @PostConstruct
    public void start() {
        if (connections == null) {
            connections = accountService.getAllConnections();
            for (XMPPConnection connection : connections) {
                connection.setController(this);
                if (!connection.isConnected())
                    new Thread(connection).start();
            }
        }
    }

    public void stop() {
        if (connections != null) {
            for (XMPPConnection connection : connections) {
                connection.close();
            }
            connections = null;
        }
    }

    public void disconnectAccount(XMPPAccount account) {
        for (XMPPConnection connection : connections) {
            if (connection.equalsByXMPPAccount(account)) {
                connection.close();
                connections.remove(connection);
                return;
            }
        }
    }

    public void connectAccount(XMPPAccount account) {
        XMPPConnection connection = new XMPPConnection(account);
        connection.setController(this);
        if (connections==null) connections = new ArrayList<>();
        for (XMPPConnection temp : connections) {
            if (temp.equalsByXMPPAccount(account)) {
                temp.close();
                connections.remove(temp);
                break;
            }
        }
        connections.add(connection);
        new Thread(connection).start();
    }

    public void receiveXMPPMessage(String server, String login, String contact, String text) {
        synchronized (this) {
            XMPPAccount account = accountService.get(server, login);
            messageService.messageFromXMPP(account, contact, text);
        }
    }

    public void sendXMPPMessage(TransferMessage transferMessage) {
        for (XMPPConnection connection : connections) {
            if (connection.equalsByXMPPAccount(transferMessage.getChatMap().getXmppAccount())) {
                connection.sendMessage(transferMessage);
                break;
            }
        }
    }
}
