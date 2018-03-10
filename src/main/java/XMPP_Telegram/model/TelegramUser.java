package XMPP_Telegram.model;

import java.util.ArrayList;
import java.util.List;

public class TelegramUser {

    private Integer id;

    private String name;

    private List<XMPPAccount> accounts = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<XMPPAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(XMPPAccount account) {
        accounts.add(account);
    }

    public XMPPAccount getAccountById(int id) {
        for (XMPPAccount account : accounts) {
            if (account.getId() == id)
                return account;
        }
        return null;
    }

    public void deleteAccount(XMPPAccount account) {
        accounts.remove(account);
    }
}
