package xmpptelegram.repository;

import xmpptelegram.model.ChatMap;
import xmpptelegram.model.TelegramUser;
import xmpptelegram.model.XMPPAccount;

import java.util.List;

public interface ChatMapRepository {
    List<ChatMap> getAll();

    ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact);

    ChatMap getByUserAndAccountAndContact(XMPPAccount account, String contact);

    void create (ChatMap chatMap) throws Exception;

    ChatMap update (ChatMap chatMap);

    ChatMap getByChatId(long chatId);

    void delete (ChatMap chatMap);
}
