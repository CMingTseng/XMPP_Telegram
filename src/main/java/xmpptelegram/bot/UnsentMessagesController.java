package xmpptelegram.bot;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xmpptelegram.model.UnsentMessage;
import xmpptelegram.service.MessageService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class UnsentMessagesController  extends Thread{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UnsentMessagesController.class);

    private final MessageService messageService;

    @Autowired
    public UnsentMessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostConstruct
    @Override
    public synchronized void start() {
        this.setDaemon(true);
        super.start();
    }

    @Override
    public void run() {
        LOGGER.info("UnsentMessagesController started");
        while (true) {
            try {
                checkUnsentMessages();
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                LOGGER.error("Error UnsentMessagesController");
            }
        }
    }

    private void checkUnsentMessages() {
        List<UnsentMessage> list = messageService.getAll();
        if (list!=null) {
           for (UnsentMessage message : list) {
               messageService.send(message);
           }
        }
    }
}
