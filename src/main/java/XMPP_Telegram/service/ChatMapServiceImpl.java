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

    @Autowired
    private ChatMapRepository repository;

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
}
