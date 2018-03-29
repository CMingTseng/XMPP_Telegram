package XMPP_Telegram.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = ChatMap.SEND_TO_XMPP, query = "SELECT c FROM ChatMap c JOIN FETCH c.telegramUser WHERE c.telegramUser=:telegramUser AND c.chatId=:chatid"),
        @NamedQuery(name = ChatMap.SEND_TO_Telegram, query = "SELECT c FROM ChatMap c JOIN FETCH c.xmppAccount WHERE c.xmppAccount=:xmppAccount AND c.xmppContact=:xmppContact"),
        @NamedQuery(name = ChatMap.GET_BY_ACCOUNT_CONTACT, query = "SELECT c FROM ChatMap c JOIN FETCH c.xmppAccount WHERE c.xmppAccount=:xmppAccount AND c.xmppContact=:xmppContact")
})
@Entity
@Table(name = "telegram_chats", uniqueConstraints = @UniqueConstraint(columnNames = {"chatid", "xmppaccount", "xmppcontact"}, name = "telegram_chats_index"))
public class ChatMap {
    public static final String SEND_TO_XMPP = "ChatMap.getByTelegramUserAndChat";
    public static final String SEND_TO_Telegram = "ChatMap.getByXMPPAccountAndContact";
    public static final String GET_BY_ACCOUNT_CONTACT = "ChatMap.getByAccountAndContact";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chatid")
    @NotNull
    private long chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "xmppaccount", nullable = false)
    @NotNull
    private XMPPAccount xmppAccount;

    @Column(name = "xmppcontact")
    @NotNull
    private String xmppContact;

    public ChatMap() {
    }

    public ChatMap(@NotNull @NotBlank long chatId, @NotNull XMPPAccount xmppAccount, @NotNull @NotBlank String xmppContact) {
        this.chatId = chatId;
        this.xmppAccount = xmppAccount;
        this.xmppContact = xmppContact;
    }

    public Long getChatId() {
        return chatId;
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
                ", xmppAccount=" + xmppAccount +
                ", xmppContact='" + xmppContact + '\'' +
                '}';
    }
}
