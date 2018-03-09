package XMPP_Telegram.controller;

import XMPP_Telegram.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

import javax.inject.Inject;

@RestController
public class RESTController {

    @Inject
    private TelegramBotService telegramBotService;

    @Autowired
    private XMPPController xmppController;

    @RequestMapping("/")
    public void test() {}

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

    @RequestMapping(value = "/${TELEGRAM_TOKEN}", method = RequestMethod.POST)
    @ResponseBody
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotService.onWebhookUpdateReceived(update);
    }

    public void setXmppController(XMPPController xmppController) {
        this.xmppController = xmppController;
    }
}
