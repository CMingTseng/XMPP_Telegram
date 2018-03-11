package XMPP_Telegram.repository;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface ChatMapRepository {
    List<ChatMap> getAll();

    ChatMap sendToXMPP(TelegramUser telegramUser, long chatId);

    ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact);
}
