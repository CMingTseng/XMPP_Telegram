package XMPP_Telegram.service;

import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface XMPPAccountService {

    List<XMPPAccount> getAll();

    XMPPAccount get(String server, String login);

    boolean delete(XMPPAccount account);

    XMPPAccount update(XMPPAccount account, String server, String login, String password, int port);

    XMPPAccount create(String server, String login, String password, int port);
}
