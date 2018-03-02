package XMPP_Telegram.model;

import XMPP_Telegram.service.XMPPAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class XMPPServer {
    private List<XMPPAccount> accounts;

    @Autowired
    private XMPPAccountService accountService;

    @PostConstruct
    public void start() {
        accounts = accountService.getAll();
        for (XMPPAccount account : accounts) {
            account.Connect();
        }
    }

    public void disconnectAccount(XMPPAccount account) {}

    public void connectAccount (XMPPAccount account) {}

    public XMPPAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(XMPPAccountService accountService) {
        this.accountService = accountService;
    }

    public List<XMPPAccount> getAccounts() {
        return accounts;
    }
}
