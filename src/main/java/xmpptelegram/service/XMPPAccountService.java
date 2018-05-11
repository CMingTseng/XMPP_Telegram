package xmpptelegram.service;

import org.slf4j.LoggerFactory;
import xmpptelegram.model.TelegramUser;
import xmpptelegram.model.XMPPAccount;
import xmpptelegram.model.XMPPConnection;
import xmpptelegram.repository.XMPPAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class XMPPAccountService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(XMPPAccountService.class);

    private final XMPPAccountRepository repository;

    @Autowired
    public XMPPAccountService(XMPPAccountRepository repository) {
        this.repository = repository;
    }

    public List<XMPPConnection> getAllConnections() {
        List<XMPPAccount> list = repository.getAll();
        List<XMPPConnection> result = new ArrayList<>();
        for (XMPPAccount account : list) {
            if (account.isActive())
                result.add(new XMPPConnection(account));
        }
        return result;
    }

    public XMPPAccount get(String server, String login) {
        return repository.get(server, login);
    }

    public boolean delete(XMPPAccount account) {
        return repository.delete(account) > 0;
    }

    public XMPPAccount update(XMPPAccount account) {
            return repository.update(account);
    }

    public boolean create(TelegramUser user, String server, String login, String password, int port) {
        if (repository.get(server, login) == null) {
            XMPPAccount account = new XMPPAccount(server, login, password, port);
            account.setTelegramUser(user);
            repository.create(account);
            return true;
        } else return false;
    }

    public List<XMPPAccount> getAllByUser(TelegramUser user) {
        List <XMPPAccount> result = repository.getAllByUser(user.getId());
        if (result == null)
            result = new ArrayList<>();
        return result;
    }

    public XMPPAccount getById(int id) {
        return repository.getById(id);
    }
}
