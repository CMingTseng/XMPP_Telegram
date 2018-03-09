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
    public TelegramUser findById(int id) {
        return null;
    }

    @Override
    public TelegramUser findByName(String name) {
        return null;
    }

    @Override
    public List<TelegramUser> findAll() {
        return null;
    }

    @Override
    public boolean validate(int id, int level) {
        return false;
    }

    public void setRepository(TelegramUserRepository repository) {
        this.repository = repository;
    }
}
