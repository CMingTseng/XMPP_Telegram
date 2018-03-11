package XMPP_Telegram.model;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = XMPPAccount.ALL, query = "SELECT x FROM XMPPAccount x"),
        @NamedQuery(name = XMPPAccount.GET_BY_LOGIN_AND_SERVER, query = "SELECT x FROM XMPPAccount x WHERE x.server=:server AND x.login=:login")
})
@Entity
@Table(name = "xmpp_accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"login", "server"}, name = "xmpp_accounts_login_server_index")})
public class XMPPAccount {
    public static final String ALL = "XMPPAccount.getAllConnections";
    public static final String GET_BY_LOGIN_AND_SERVER = "XMPPAccount.getByLoginAndServer";


    @Id
    private int id;

    @Column(name = "server")
    @NotNull
    @NotBlank
    private String server;

    @Column(name = "login")
    @NotNull
    @NotBlank
    private String login;

    @Column(name = "password")
    @NotNull
    @NotBlank
    private String password;

    @Column(name = "port", columnDefinition = "5222")
    @NotNull
    @NotBlank
    private int port;

    @Column(name = "savehistory", columnDefinition = "1")
    @NotNull
    @NotBlank
    private boolean saveHistory;

    @Column(name = "active", columnDefinition = "1")
    @NotNull
    @NotBlank
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telegramuser", nullable = false)
    @NotNull
    private TelegramUser telegramUser;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "xmppaccount", nullable = false)
//    private Set<ChatMap> map;

    public XMPPAccount() {
    }

    public XMPPAccount(String server, String login, String password) {
        this.server = server;
        this.login = login;
        this.password = password;
        port = 5222;
    }

    public XMPPAccount(String server, String login, String password, int port) {
        this.server = server;
        this.login = login;
        this.password = password;
        this.port = port;
    }

    public boolean isActive() {
        return active;
    }

    public int getPort() {
        return port;
    }

    public String getServer() {
        return server;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSaveHistory() {
        return saveHistory;
    }

    public int getId() {
        return id;
    }

//    public Set<ChatMap> getMap() {
//        return map;
//    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

}
