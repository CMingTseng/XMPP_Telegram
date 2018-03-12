package XMPP_Telegram.controller;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.model.XMPPConnection;
import XMPP_Telegram.service.MessageService;
import XMPP_Telegram.service.XMPPAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
public class XMPPController {
    private List<XMPPConnection> connections;

    private XMPPAccountService accountService;

    private MessageService messageService;

    @Autowired
    public XMPPController(XMPPAccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostConstruct
    public void start() {
        if (connections == null) {
            connections = accountService.getAllConnections();
            for (XMPPConnection connection : connections) {
                connection.setController(this);
                if (!connection.isConnected())
                    try {
                        connection.createConnection();
                    } catch (Exception e) {
                        //TODO
                    }
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
        connections.add(connection);
        try {
            connection.createConnection();
        } catch (Exception e) {
            //TODO
        }
    }

    public void receivedMessage(String server, String login, String contact, String text) {
        XMPPAccount account = accountService.get(server, login);
        messageService.messageFromXMPP(account, contact, text);
    }

    public void sendMessage(ChatMap map, String text) {
        for (XMPPConnection connection : connections) {
            if (connection.equalsByXMPPAccount(map.getXmppAccount())) {
                connection.sendMessage(map, text);
                return;
            }
        }
    }
}
