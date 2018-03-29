package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface ChatMapService {
    List<ChatMap> getAll();

    ChatMap sendToXMPP(TelegramUser telegramUser, long chatId);

    ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact);

    ChatMap create(XMPPAccount account, long chatId, String contact);

    ChatMap getByAccountAndContact(XMPPAccount account, String contact);
}
