package XMPP_Telegram.repository;

import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface XMPPAccountRepository {
    List<XMPPAccount> getAll();

    XMPPAccount get(String server, String login);

    int delete(XMPPAccount account);

    int update(XMPPAccount account, String server, String login, String password, int port);

    void create (String server, String login, String password, int port);

    XMPPAccount getById(int id);
}
