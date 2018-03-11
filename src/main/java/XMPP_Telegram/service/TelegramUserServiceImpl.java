package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    @Autowired
    private TelegramUserRepository repository;

    @Override
    public TelegramUser create(int id, String name, int level) {
        return null;
    }

    @Override
    public void update(TelegramUser user) {

    }
    @Override
    public TelegramUser delete(int id) {
        return null;
    }

    @Override
    public TelegramUser getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<TelegramUser> getAll() {
        return repository.getAll();
    }
}
