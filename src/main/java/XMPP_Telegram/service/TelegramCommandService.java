package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import org.telegram.telegrambots.api.objects.Update;

public interface TelegramCommandService {
    String useCommand (Update update);

    String start (int id, String login);

    String addAccount (int userId, String server, String login, String password);

    String addAccount (int userId, String server, String login, String password, int port);

    String addGroup (int userId, long chatId, String server, String login, String contact);
}
