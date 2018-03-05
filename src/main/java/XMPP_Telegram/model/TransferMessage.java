package XMPP_Telegram.model;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

public class TransferMessage {

    private long id = 0;

    private String text;

    private XMPPAccount account;

    private String contact;

    private boolean fromXMPP;

    private boolean sent = false;

    private Date date;

    public TransferMessage() {
    }

    public TransferMessage(Message message, XMPPAccount account) {
        this.text = message.getBody();
        this.account = account;
        this.contact = message.getFrom().asBareJid().toString();
        date = new Date();
        fromXMPP = true;
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

    public XMPPAccount getAccount() {
        return account;
    }

    public void setAccount(XMPPAccount account) {
        this.account = account;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
