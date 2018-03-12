package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private TelegramWebHookService telegramWebHookService;

    @Override
    public void create(TransferMessage message) {
        repository.create(message);
    }

    @Override
    public void messageFromXMPP(XMPPAccount account, String contact, String text) {
        ChatMap map = chatMapService.sendToTelegram(account, contact);
        telegramWebHookService.sendToTelegram(map, text);
    }
}
