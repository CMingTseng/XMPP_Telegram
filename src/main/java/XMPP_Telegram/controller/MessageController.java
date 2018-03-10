package XMPP_Telegram.controller;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.service.ChatMapService;
import XMPP_Telegram.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private XMPPController xmppController;

    @Autowired
    private TelegramController telegramController;

    private Map<TelegramUser, Map<XMPPAccount, Map<String, Long>>> map;

    @Autowired
    public void init() {
        while (!xmppController.isStarted() && !telegramController.isStarted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //TODO
            }
        }
        List<ChatMap> list = chatMapService.getAll();
        map = new HashMap<>();
        if (list==null)
            return;
        for (ChatMap chatMap : list) {
            if (!map.containsKey(telegramController.getUserById(chatMap.getTelegramUser())))
                map.put(telegramController.getUserById(chatMap.getTelegramUser()),new HashMap<>());
            if (!map.get(telegramController.getUserById(chatMap.getTelegramUser())).containsKey(xmppController.getAccountById(chatMap.getXmppAccount())))
                map.get(telegramController.getUserById(chatMap.getTelegramUser())).put(xmppController.getAccountById(chatMap.getXmppAccount()), new HashMap<>());
            map.get(telegramController.getUserById(chatMap.getTelegramUser())).get(xmppController.getAccountById(chatMap.getXmppAccount())).put(chatMap.getXmppContact(),chatMap.getChatId());
        }
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setChatMapService(ChatMapService chatMapService) {
        this.chatMapService = chatMapService;
    }

    public void setXmppController(XMPPController xmppController) {
        this.xmppController = xmppController;
    }

    public void setTelegramController(TelegramController telegramController) {
        this.telegramController = telegramController;
    }
}
