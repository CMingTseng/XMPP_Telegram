package xmpptelegram.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


public class TransferMessage {

    private String text;

    private ChatMap chatMap;

    private boolean fromXMPP;

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
                "text='" + text + '\'' +
                ", chatMap=" + chatMap +
                ", fromXMPP=" + fromXMPP +
                ", date=" + date +
                '}';
    }
}
