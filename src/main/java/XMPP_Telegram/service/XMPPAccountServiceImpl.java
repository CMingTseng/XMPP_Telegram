package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.model.XMPPConnection;
import XMPP_Telegram.repository.XMPPAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class XMPPAccountServiceImpl implements XMPPAccountService {

    private final XMPPAccountRepository repository;

    @Autowired
    public XMPPAccountServiceImpl(XMPPAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<XMPPConnection> getAllConnections() {
        List<XMPPAccount> list = repository.getAll();
        List<XMPPConnection> result = new ArrayList<>();
        for (XMPPAccount account : list) {
            if (account.isActive())
                result.add(new XMPPConnection(account));
        }
        return result;
    }

    @Override
    public XMPPAccount get(String server, String login) {
        return repository.get(server, login);
    }

    @Override
    public boolean delete(XMPPAccount account) {
        return repository.delete(account) > 0;
    }

    @Override
    public XMPPAccount update(XMPPAccount account, String server, String login, String password, int port) {
//        repository.update(account, server, login, password, port);
        return repository.get(server, login);
    }

    @Override
    public boolean create(TelegramUser user, String server, String login, String password, int port) {
        if (repository.get(server, login) == null) {
            XMPPAccount account = new XMPPAccount(server, login, password, port);
            account.setTelegramUser(user);
            repository.create(account);
            return true;
        } else return false;
    }

    @Override
    public XMPPAccount getById(int id) {
        return repository.getById(id);
    }
}
