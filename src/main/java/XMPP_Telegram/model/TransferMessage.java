package XMPP_Telegram.model;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

public class TransferMessage {

    private long id = 0;

    private String text;

    private XMPPAccount xmppAccount;

    private TelegramUser telegramUser;

    private String contact;

    private long chatId;

    private boolean fromXMPP;

    private boolean sent = false;

    private Date date;

    public TransferMessage() {
    }

    public TransferMessage(Message message, XMPPAccount xmppAccount) {
        this.text = message.getBody();
        this.xmppAccount = xmppAccount;
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

    public XMPPAccount getXmppAccount() {
        return xmppAccount;
    }

    public void setXmppAccount(XMPPAccount xmppAccount) {
        this.xmppAccount = xmppAccount;
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

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "TransferMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", xmppAccount=" + xmppAccount +
                ", contact='" + contact + '\'' +
                ", fromXMPP=" + fromXMPP +
                ", sent=" + sent +
                ", date=" + date +
                '}';
    }
}
