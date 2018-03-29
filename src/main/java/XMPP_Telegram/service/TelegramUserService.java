package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    boolean create(int id, String name);

    TelegramUser update(TelegramUser user);

    boolean delete(int id);

    TelegramUser getById(int id);

    List<TelegramUser> getAll();
}
