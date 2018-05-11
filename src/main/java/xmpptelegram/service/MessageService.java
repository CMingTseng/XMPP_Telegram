package xmpptelegram.service;

import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import xmpptelegram.bot.TelegramBot;
import xmpptelegram.bot.XMPPBot;
import xmpptelegram.model.ChatMap;
import xmpptelegram.model.TransferMessage;
import xmpptelegram.model.UnsentMessage;
import xmpptelegram.model.XMPPAccount;
import xmpptelegram.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository repository;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private XMPPBot xmppBot;

    @Autowired
    private TelegramCommandService telegramCommandService;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<UnsentMessage> getAll() {
        return repository.getAll();
    }

    public void messageFromXMPP(XMPPAccount account, String contact, String text) {
        ChatMap map = chatMapService.sendToTelegram(account, contact);
        if (map == null) {
            map = new ChatMap(account.getTelegramUser().getDefaultChat(), account, contact);
            if (contact.equals("status message"))
                text = String.format("Статус аккаунта %s: %s", account.getLogin() + "@" + account.getServer(), text);
            else
                text = String.format("Сообщение для аккаунта: %s от контакта: %s \n%s", account.getLogin() + "@" + account.getServer(), contact, text);
        }
        send(new TransferMessage(map, text, true));
    }

    public synchronized void send(UnsentMessage unsentMessage) {
        TransferMessage message = new TransferMessage();
        message.setFromXMPP(unsentMessage.isFromXMPP());
        message.setText(unsentMessage.getText());
        message.setDate(unsentMessage.getDate());
        ChatMap map = chatMapService.getByAccountAndContact(unsentMessage.getXmppAccount(), unsentMessage.getXmppContact());
        if (map != null) {
            message.setChatMap(map);
            repository.delete(unsentMessage);
            send(message);
        }
    }

    private void send(TransferMessage transferMessage) {
        if (transferMessage.isFromXMPP()) {
            SendMessage message = new SendMessage();
            message.setChatId(transferMessage.getChatMap().getChatId());
            message.setText(transferMessage.getText());
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                LOGGER.error(String.format("Error sending message to Telegram! Message: %s", transferMessage.toString()), e);
                repository.create(new UnsentMessage(transferMessage));
            }
        } else {
            try {
                xmppBot.sendXMPPMessage(transferMessage);
            } catch (Exception e) {
                LOGGER.error(String.format("Error sending message to XMPP! Message: %s", transferMessage.toString()), e);
                repository.create(new UnsentMessage(transferMessage));
            }
        }
    }

    private void sendNotification(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Error sending notification to Telegram! Message: %s, ChatId: %d", text, chatId), e);
        }
    }

    public void messageFromTelegram(Update update) {
        ChatMap map = chatMapService.getByChatId(update.getMessage().getChatId());
        if (update.getMessage().getText().matches("^[/].+")) {
            sendNotification(telegramCommandService.useCommand(update), update.getMessage().getChatId());
        } else {
            if (update.getMessage().getChatId().equals((long) update.getMessage().getFrom().getId()) || map == null) {
                LOGGER.warn("Incorrect chat. ", update.toString());
                return;
            }
            send(new TransferMessage(map, update.getMessage().getText(), false));
        }
    }
}
