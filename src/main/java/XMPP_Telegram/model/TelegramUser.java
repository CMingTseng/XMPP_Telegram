package XMPP_Telegram.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = TelegramUser.ALL, query = "SELECT t FROM TelegramUser t"),
        @NamedQuery(name = TelegramUser.GET_BY_ID, query = "SELECT t FROM TelegramUser t WHERE t.id=:id"),
})
@Entity
@Table(name = "telegram_users")
public class TelegramUser {
    public static final String ALL = "TelegramUser.getAllConnections";
    public static final String GET_BY_ID = "TelegramUser.getById";

    @Id
    private Integer id;

    @Column(name = "username")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "telegramUser", cascade = CascadeType.ALL)
    private Set<XMPPAccount> accounts = new HashSet<>();

    public TelegramUser() {
    }

    public TelegramUser(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
