package XMPP_Telegram.controller;

import XMPP_Telegram.service.ChatMapService;
import XMPP_Telegram.service.TelegramWebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Map;

@RestController
public class RESTController {

    @Autowired
    private TelegramWebHookService telegramWebHookService;

    @Autowired
    private XMPPController xmppController;

    @RequestMapping("/")
    public String test() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Thread,StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()){
            sb.append(entry.getKey().getName()).append(" ").append(entry.getKey().getState()).append(System.lineSeparator());
        }
        return sb.toString();
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
