package xmpptelegram.service;

import org.slf4j.LoggerFactory;
import xmpptelegram.model.TelegramUser;
import xmpptelegram.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUserService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TelegramUserService.class);

    private final TelegramUserRepository repository;

    @Autowired
    public TelegramUserService(TelegramUserRepository repository) {
        this.repository = repository;
    }

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

    public TelegramUser update(TelegramUser user) {
            try {
                repository.update(user);
                return repository.getById(user.getId());
            } catch (Exception e) {
                return null;
            }
    }

    public boolean delete(int id) {
        if (getById(id) != null) {
            try {
                repository.delete(getById(id));
                return true;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }

    public TelegramUser getById(int id) {
        return repository.getById(id);
    }

    public List<TelegramUser> getAll() {
        return repository.getAll();
    }
}
