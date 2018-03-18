package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import org.telegram.telegrambots.api.objects.Update;

public interface TelegramCommandService {
    String useCommand (Update update);

    void start (int id, String login);

    void addGroup (TelegramUser user, long chatId, XMPPAccount account, String contact);
}
