package XMPP_Telegram.controller;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.model.XMPPConnection;
import XMPP_Telegram.service.MessageService;
import XMPP_Telegram.service.TelegramWebHookService;
import XMPP_Telegram.service.XMPPAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
public class XMPPController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    private List<XMPPConnection> connections;

    private XMPPAccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public XMPPController(XMPPAccountService accountService) {
        this.accountService = accountService;
    }

    @PostConstruct
    public void start() {
        if (connections == null) {
            connections = accountService.getAllConnections();
            for (XMPPConnection connection : connections) {
                connection.setController(this);
                if (!connection.isConnected())
                    try {
                        connection.start();
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
        for (XMPPConnection temp : connections) {
            if (temp.equalsByXMPPAccount(account)) {
                temp.close();
                connections.remove(temp);
                break;
            }
        }
        connections.add(connection);
        connection.start();
    }

    public void receiveXMPPMessage(String server, String login, String contact, String text) {
        XMPPAccount account = accountService.get(server, login);
        messageService.messageFromXMPP(account, contact, text);
    }

    public void sendXMPPMessage(ChatMap map, String text) {
        for (XMPPConnection connection : connections) {
            if (connection.equalsByXMPPAccount(map.getXmppAccount())) {
                connection.sendMessage(map, text);
                return;
            }
        }
    }
}
