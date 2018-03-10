package XMPP_Telegram.model;


public class ChatMap {

    private long chatId;

    private int telegramUser;

    private int xmppAccount;

    private String xmppContact;

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setXmppAccount(int xmppAccount) {
        this.xmppAccount = xmppAccount;
    }

    public void setXmppContact(String xmppContact) {
        this.xmppContact = xmppContact;
    }

    public void setTelegramUser(int telegramUser) {
        this.telegramUser = telegramUser;
    }

    public Long getChatId() {
        return chatId;
    }

    public int getTelegramUser() {
        return telegramUser;
    }

    public int getXmppAccount() {
        return xmppAccount;
    }

    public String getXmppContact() {
        return xmppContact;
    }
}
