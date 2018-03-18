package XMPP_Telegram.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = ChatMap.SEND_TO_XMPP, query = "SELECT c FROM ChatMap c JOIN FETCH c.telegramUser WHERE c.telegramUser=:telegramUser AND c.chatId=:chatid"),
        @NamedQuery(name = ChatMap.SEND_TO_Telegram, query = "SELECT c FROM ChatMap c JOIN FETCH c.xmppAccount WHERE c.xmppAccount=:xmppAccount AND c.xmppContact=:xmppContact"),
        @NamedQuery(name = ChatMap.GET_BY_USER_ACCOUNT_CONTACT, query = "SELECT c FROM ChatMap c JOIN FETCH c.xmppAccount JOIN FETCH c.telegramUser WHERE c.xmppAccount=:xmppAccount AND c.xmppContact=:xmppContact AND c.telegramUser=:telegramUser")
})
@Entity
@Table(name = "telegram_chats", uniqueConstraints = @UniqueConstraint(columnNames = {"chatid", "telegramuser", "xmppaccount", "xmppcontact"}, name = "telegram_chats_index"))
public class ChatMap {
    public static final String SEND_TO_XMPP = "ChatMap.getByTelegramUserAndChat";
    public static final String SEND_TO_Telegram = "ChatMap.getByXMPPAccountAndContact";
    public static final String GET_BY_USER_ACCOUNT_CONTACT = "ChatMap.getByUserAndAccountAndContact";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "chatid")
    @NotNull
    @NotBlank
    private long chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "telegramuser", nullable = false)
    @NotNull
    private TelegramUser telegramUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "xmppaccount", nullable = false)
    @NotNull
    private XMPPAccount xmppAccount;

    @Column(name = "xmppcontact")
    @NotNull
    @NotBlank
    private String xmppContact;

    public ChatMap() {
    }

    public ChatMap(@NotNull @NotBlank long chatId, @NotNull TelegramUser telegramUser, @NotNull XMPPAccount xmppAccount, @NotNull @NotBlank String xmppContact) {
        this.chatId = chatId;
        this.telegramUser = telegramUser;
        this.xmppAccount = xmppAccount;
        this.xmppContact = xmppContact;
    }

    public Long getChatId() {
        return chatId;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public XMPPAccount getXmppAccount() {
        return xmppAccount;
    }

    public String getXmppContact() {
        return xmppContact;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public void setXmppAccount(XMPPAccount xmppAccount) {
        this.xmppAccount = xmppAccount;
    }

    public void setXmppContact(String xmppContact) {
        this.xmppContact = xmppContact;
    }

    @Override
    public String toString() {
        return "ChatMap{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", telegramUser=" + telegramUser +
                ", xmppAccount=" + xmppAccount +
                ", xmppContact='" + xmppContact + '\'' +
                '}';
    }
}
