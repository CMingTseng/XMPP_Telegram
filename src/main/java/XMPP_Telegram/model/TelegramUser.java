package XMPP_Telegram.model;

import java.time.LocalDateTime;

public class TelegramUser {
    private Integer id;

    private String name;
    private Integer level;

    protected LocalDateTime createdDate;
    protected LocalDateTime modifiedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public TelegramUser() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    public TelegramUser(int id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
