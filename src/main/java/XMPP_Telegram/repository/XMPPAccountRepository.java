package XMPP_Telegram.repository;

import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface XMPPAccountRepository {
    List<XMPPAccount> getAll();

    XMPPAccount get(String server, String login);

    int delete(XMPPAccount account);

    XMPPAccount update(XMPPAccount account);

    void create (XMPPAccount account);

    XMPPAccount getById(int id);
}
