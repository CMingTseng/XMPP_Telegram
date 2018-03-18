package XMPP_Telegram.service;

import XMPP_Telegram.controller.XMPPController;
import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
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

    @Autowired
    private XMPPController xmppController;

    @Override
    public void create(TransferMessage message) {
        repository.create(message);
    }

    @Override
    public void messageFromXMPP(XMPPAccount account, String contact, String text) {
        ChatMap map = chatMapService.sendToTelegram(account, contact);
        if (map == null) {
            map = new ChatMap();
            map.setXmppContact(contact);
            map.setXmppAccount(account);
            map.setTelegramUser(account.getTelegramUser());
            map.setChatId(account.getTelegramUser().getId());
            text = String.format("Сообщение для аккаунта: %s от контакта: %s \n%s", account.getLogin() + "@" + account.getServer(), contact, text);
        }
        telegramWebHookService.sendTelegramMessage(map, text);
    }

    @Override
    public void messageFromTelegram(TelegramUser user, long chatId, String text) {
        ChatMap map = chatMapService.sendToXMPP(user, chatId);
        if (map==null) {
            return;
            //TODO
        }
        xmppController.sendXMPPMessage(map, text);
    }


}
