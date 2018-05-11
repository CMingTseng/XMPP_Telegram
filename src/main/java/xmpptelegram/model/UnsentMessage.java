package xmpptelegram.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@SuppressWarnings("JpaQlInspection")
@Entity
@Table(name = "unsent_messages", uniqueConstraints = @UniqueConstraint(columnNames = "date", name = "messages_dtinput_index"))
@NamedQueries({
        @NamedQuery(name = UnsentMessage.GET_ALL, query = "SELECT m FROM UnsentMessage m ORDER BY m.id")
})
public class UnsentMessage {
    public static final String GET_ALL = "getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "xmppaccount", nullable = false)
    @NotNull
    private XMPPAccount xmppAccount;

    @Column(name = "fromXMPP", nullable = false)
    @NotNull
    private boolean fromXMPP;

    @Column(name = "xmppcontact")
    @NotNull
    private String xmppContact;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public UnsentMessage(TransferMessage transferMessage) {
        text = transferMessage.getText();
        xmppAccount = transferMessage.getChatMap().getXmppAccount();
        fromXMPP = transferMessage.isFromXMPP();
        xmppContact = transferMessage.getChatMap().getXmppContact();
        date = new Date();
    }

    public String getText() {
        return text;
    }

    public XMPPAccount getXmppAccount() {
        return xmppAccount;
    }

    public boolean isFromXMPP() {
        return fromXMPP;
    }

    public String getXmppContact() {
        return xmppContact;
    }

    public Date getDate() {
        return date;
    }
}
