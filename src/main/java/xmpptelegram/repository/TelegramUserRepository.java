package xmpptelegram.repository;

import xmpptelegram.model.TelegramUser;

import java.util.List;

public interface TelegramUserRepository {
    List<TelegramUser> getAll();

    TelegramUser getById (int id);

    void delete(TelegramUser user) throws Exception;

    void update(TelegramUser user) throws Exception;

    void create (int id, String username) throws Exception;


}
