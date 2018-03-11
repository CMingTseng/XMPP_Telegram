package XMPP_Telegram.repository;

import XMPP_Telegram.model.TelegramUser;

import java.util.List;

public interface TelegramUserRepository {
    List<TelegramUser> getAll();

    TelegramUser getById (int id);

    int delete(TelegramUser user);

    int update(TelegramUser user, String name);

    void create (int id, String username);


}
