package XMPP_Telegram.model;

import org.jivesoftware.smack.packet.Message;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@SuppressWarnings("JpaQlInspection")
@Entity
@Table(name = "messages", uniqueConstraints = @UniqueConstraint(columnNames = "date", name = "messages_dtinput_index"))
public class TransferMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatmap", nullable = false)
    private ChatMap chatMap;

    @Column(name = "fromXMPP", nullable = false)
    @NotNull
    private boolean fromXMPP;

    @Column(name = "date")
    private Date date;

    public TransferMessage() {
    }

    public ChatMap getChatMap() {
        return chatMap;
    }

    public void setChatMap(ChatMap chatMap) {
        this.chatMap = chatMap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFromXMPP() {
        return fromXMPP;
    }

    public void setFromXMPP(boolean fromXMPP) {
        this.fromXMPP = fromXMPP;
    }

    @Override
    public String toString() {
        return "TransferMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", fromXMPP=" + fromXMPP +
                ", date=" + date +
                '}';
    }
}
