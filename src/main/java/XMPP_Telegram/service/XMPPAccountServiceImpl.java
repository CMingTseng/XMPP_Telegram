package XMPP_Telegram.service;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XMPPAccountServiceImpl implements XMPPAccountService {

    @Autowired
    private XMPPAccountRepository repository;

    @Override
    public List<XMPPAccount> getAll() {
        return repository.getAll();
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
        repository.update(account, server, login, password, port);
        return repository.get(server, login);
    }

    @Override
    public XMPPAccount create(String server, String login, String password, int port) {
        repository.create(server, login, password, port);
        return get(server, login);
    }

    public void setRepository(XMPPAccountRepository repository) {
        this.repository = repository;
    }
}
