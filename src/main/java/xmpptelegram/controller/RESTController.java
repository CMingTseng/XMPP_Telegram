package xmpptelegram.controller;

import org.slf4j.LoggerFactory;
import xmpptelegram.bot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import xmpptelegram.bot.XMPPBot;
import xmpptelegram.model.ChatMap;
import xmpptelegram.model.TransferMessage;
import xmpptelegram.repository.MessageRepository;
import xmpptelegram.service.ChatMapService;

@RestController
public class RESTController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RESTController.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private XMPPBot xmppBot;

    @RequestMapping("/")
    public String test() {
        return null;
    }

    @RequestMapping("/secured")
    public String secured(){
        System.out.println("Inside secured()");
        return "Hello user !!!";
    }

    @RequestMapping("/stop")
    public String stop(){
        xmppBot.stop();
        return "XMPP server stoped";
    }
    @RequestMapping("/start")
    public String start(){
        xmppBot.start();
        return "XMPP server started";
    }

    @RequestMapping(value = "/${telegram.token}", method = RequestMethod.POST)
    @ResponseBody
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
