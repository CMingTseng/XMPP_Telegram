package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.model.XMPPConnection;

import java.util.List;

public interface XMPPAccountService {

    List<XMPPConnection> getAllConnections();

    XMPPAccount get(String server, String login);

    boolean delete(XMPPAccount account);

    XMPPAccount update(XMPPAccount account, String server, String login, String password, int port);

    boolean create(TelegramUser user, String server, String login, String password, int port);

    XMPPAccount getById(int id);

}
