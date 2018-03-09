package XMPP_Telegram.controller;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.service.MessageService;
import XMPP_Telegram.service.XMPPAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class XMPPController {
    private List<XMPPAccount> accounts;

    @Autowired
    private XMPPAccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public void start() {
        accounts = accountService.getAll();
        for (XMPPAccount account : accounts) {
            account.setController(this);
            if (account.isActive())
                account.connect();
        }
    }

    public void stop() {
        for (XMPPAccount account : accounts) {
            account.disconnect();
        }
    }

    public void disconnectAccount(XMPPAccount account) {
        account.disconnect();
    }

    public void connectAccount (XMPPAccount account) {
        account.connect();
    }

    public void saveMessage (TransferMessage message) {
        messageService.create(message);

    }

    public void setAccountService(XMPPAccountService accountService) {
        this.accountService = accountService;
    }

    public List<XMPPAccount> getAccounts() {
        return accounts;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
