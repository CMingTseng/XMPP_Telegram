package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    TelegramUser create(int id, String name, int level);

    void update(TelegramUser user);

    TelegramUser delete(int id);

    TelegramUser getById(int id);

    List<TelegramUser> getAll();
}
