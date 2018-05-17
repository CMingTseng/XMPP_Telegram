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

    private volatile List<XMPPConnection> connections;

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
        new StatusChecker(this).start();

    }

    public void stop() {
        synchronized (connections) {
            if (connections != null) {
                for (XMPPConnection connection : connections) {
                    connection.close();
                }
                connections = null;
            }
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
        if (connections == null) connections = new ArrayList<>();
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

    public String checkStatus(XMPPAccount account) {
        for (XMPPConnection connection : connections) {
            if (connection.equalsByXMPPAccount(account)) {
                return connection.isConnected() ? "Подключен" : "Не в сети";
            }
        }
        return "Нет аккаунта";
    }

    private class StatusChecker extends Thread {

        private XMPPBot controller;

        StatusChecker(XMPPBot controller) {
            this.setDaemon(true);
            this.controller = controller;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300000);
                    synchronized (connections) {
                        for (int i = 0; i < connections.size(); i++) {
                            if (!connections.get(i).isConnected()) {
                                XMPPConnection connection = new XMPPConnection(accountService.get(connections.get(i).getServer(), connections.get(i).getLogin()));
                                connections.get(i).close();
                                connections.add(i, connection);
                                connection.setController(controller);
                                if (!connection.isConnected())
                                    new Thread(connection).start();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("Error status checker\n" + e.getMessage());
                }
            }
        }
    }
}
