package XMPP_Telegram.controller;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.service.MessageService;
import XMPP_Telegram.service.XMPPAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
public class XMPPController {
    private List<XMPPAccount> accounts;

    @Autowired
    private XMPPAccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostConstruct
    public void start() {
        accounts = accountService.getAll();
        for (XMPPAccount account : accounts) {
            account.connect();
        }
    }

    public void disconnectAccount(XMPPAccount account) {
        account.disconnect();
    }

    public void connectAccount (XMPPAccount account) {
        account.connect();
    }

    public void saveMessage (TransferMessage message) {

    }

    public XMPPAccountService getAccountService() {
        return accountService;
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
