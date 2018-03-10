package XMPP_Telegram.controller;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.service.TelegramUserService;
import XMPP_Telegram.service.TelegramWebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TelegramController {
    private List<TelegramUser> users;

    private boolean started = false;

    @Autowired
    private XMPPController xmppController = null;

    @Autowired
    private TelegramUserService userService;

    @Autowired
    private MessageController messageController;

    @Autowired
    private TelegramWebHookService webHookService;

    @Autowired
    private void init() {
        while (xmppController == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //TODO
            }
        }
        xmppController.start();
        started = true;
    }

    public TelegramUser getUserById(int id) {
        for (TelegramUser user : users) {
            if (user.getId() == id)
                return user;
        }
        return null;
    }

    public void setUserService(TelegramUserService userService) {
        this.userService = userService;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    public void setWebHookService(TelegramWebHookService webHookService) {
        this.webHookService = webHookService;
    }

    public void setUsers(List<TelegramUser> users) {
        this.users = users;
    }

    public boolean isStarted() {
        return started;
    }

    public void setXmppController(XMPPController xmppController) {
        this.xmppController = xmppController;
    }
}
