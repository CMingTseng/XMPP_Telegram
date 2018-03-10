package XMPP_Telegram.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "telegram_users")
public class TelegramUser {

    @Id
    private Integer id;

    @Column(name = "username")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "xmpp_accounts", cascade = CascadeType.ALL)
    private Set<XMPPAccount> accounts = new HashSet<>();

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

    public Set<XMPPAccount> getAccounts() {
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
