package xmpptelegram.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


public class TransferMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatmap")
    private ChatMap chatMap;

    @Column(name = "fromXMPP", nullable = false)
    @NotNull
    private boolean fromXMPP;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public TransferMessage() {
    }

    public TransferMessage(ChatMap map, String text, boolean fromXMPP) {
        this.chatMap=map;
        this.text=text;
        this.fromXMPP=fromXMPP;
    }

    public ChatMap getChatMap() {
        return chatMap;
    }

    public void setChatMap(ChatMap chatMap) {
        this.chatMap = chatMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
                ", chatMap=" + chatMap +
                ", fromXMPP=" + fromXMPP +
                ", date=" + date +
                '}';
    }
}
