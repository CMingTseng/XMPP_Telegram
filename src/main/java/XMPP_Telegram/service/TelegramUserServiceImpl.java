package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository repository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean create(int id, String name) {
        if (getById(id) == null) {
            try {
                repository.create(id, name);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }

    @Override
    public TelegramUser update(TelegramUser user) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
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
