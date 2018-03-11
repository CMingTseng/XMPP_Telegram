package XMPP_Telegram.controller;

import XMPP_Telegram.service.ChatMapService;
import XMPP_Telegram.service.TelegramWebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

@RestController
public class RESTController {

    @Autowired
    private TelegramWebHookService telegramWebHookService;

    @Autowired
    private XMPPController xmppController;

    @Autowired
    private ChatMapService mapService;

    @RequestMapping("/")
    public String test() {
        return "test";
    }

    @RequestMapping("/secured")
    public String secured(){
        System.out.println("Inside secured()");
        return "Hello user !!!";
    }

    @RequestMapping("/stop")
    public String stop(){
        xmppController.stop();
        return "XMPP server stoped";
    }
    @RequestMapping("/start")
    public String start(){
        xmppController.start();
        return "XMPP server started";
    }

    @RequestMapping(value = "/${telegram.token}", method = RequestMethod.POST)
    @ResponseBody
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramWebHookService.onWebhookUpdateReceived(update);
    }
}
