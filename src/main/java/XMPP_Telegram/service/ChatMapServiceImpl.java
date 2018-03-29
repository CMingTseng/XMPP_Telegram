package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.ChatMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMapServiceImpl implements ChatMapService {

    private final ChatMapRepository repository;

    @Autowired
    public ChatMapServiceImpl(ChatMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChatMap> getAll() {
        return repository.getAll();
    }

    @Override
    public ChatMap sendToXMPP(TelegramUser telegramUser, long chatId) {
        return repository.sendToXMPP(telegramUser, chatId);
    }

    @Override
    public ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact) {
        return repository.sendToTelegram(xmppAccount, contact);
    }

    @Override
    public ChatMap create(XMPPAccount account, long chatId, String contact) {
        if (getByAccountAndContact(account, contact) == null) {
            ChatMap map = new ChatMap(chatId, account, contact);
            repository.create(map);
        } else {
            ChatMap map = getByAccountAndContact(account, contact);
            map.setChatId(chatId);
            repository.update(map);
        }
        return repository.getByUserAndAccountAndContact(account,contact);
    }

    @Override
    public ChatMap getByAccountAndContact(XMPPAccount account, String contact) {
        return repository.getByUserAndAccountAndContact(account,contact);
    }
}
