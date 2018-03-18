package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import org.telegram.telegrambots.api.objects.Update;

public interface TelegramCommandService {
    String useCommand (Update update);

    void start (int id, String login);

    boolean addAccount (int userId, String server, String login, String password);

    boolean addAccount (int userId, String server, String login, String password, int port);

    boolean addGroup (TelegramUser user, long chatId, XMPPAccount account, String contact);
}
