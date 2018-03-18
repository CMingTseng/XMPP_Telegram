package XMPP_Telegram.service;

import XMPP_Telegram.config.TelegramConfig;
import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.util.BotUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiConstants;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.WebhookInfo;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@Service
public class TelegramWebHookServiceImpl extends TelegramWebHookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TelegramConfig config;

    @Autowired
    private TelegramCommandService telegramCommandService;

    @Autowired
    private void init(TelegramConfig config) throws Exception {
        final WebhookInfo info = getWebhookInfo();
        final String url = info.getUrl();
        final String webHookUrl = config.getPath() + config.getToken();
        LOGGER.info("Verifying web hook..");
        if (url == null || url.isEmpty() || !url.equals(webHookUrl)) {
            LOGGER.info("Web Hook URL require changes updating to: {} ..", webHookUrl);
            setWebhook(webHookUrl, "");
        } else {
            LOGGER.info("Web Hook is okay {}", webHookUrl);
        }
    }

    //    XMPP support nothing except text messages. All another message's type back notification
    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        if (update != null) {
            try {
                if (update.hasChosenInlineQuery() || update.hasInlineQuery() || update.hasCallbackQuery() || update.hasEditedMessage()) {
                    // Wrong message type
                    sendNotificationAboutWrongMessageType(update);
                } else if (update.hasMessage()) {
                    LOGGER.info("Message to bot: "+ update.toString());
                    receiveTelegramMessage(update);
                    // Handle message
                } else {
                    LOGGER.warn("Update doesn't contains neither ChosenInlineQuery/InlineQuery/CallbackQuery/EditedMessage/Message Update: {}", update);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to handle incoming update", e);
            }
        }
        return null;
    }

    public void sendTelegramMessage(ChatMap map, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(map.getChatId());
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Failed to send message! %s", text, e);
        }
    }

    private void receiveTelegramMessage(Update update) {
        if (update.getMessage().getText().matches("^[/].+")){
            sendBackMessage(update,telegramCommandService.useCommand(update));
            return;
        }
        TelegramUser user = telegramUserService.getById(update.getMessage().getFrom().getId());
        messageService.messageFromTelegram(user, update.getMessage().getChatId(), update.getMessage().getText());
    }

    private void sendNotificationAboutWrongMessageType(Update update) {
        LOGGER.info("Wrong message: " + update.toString());
        SendMessage message = new SendMessage();
        message.setText("Данный запрос не поддерживается ботом!");
        message.setReplyToMessageId(update.getMessage().getMessageId());
        try {
            LOGGER.info("Message type is not supported!", update);
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Failed to send message!", message);
        }
    }

    private void sendBackMessage (Update update, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Failed to send message! %s", text, e);
        }
    }

    @Override
    public void setWebhook(String url, String publicCertificatePath) throws TelegramApiRequestException {
        try {
            final SetWebhook setWebhook = new SetWebhook();
            setWebhook.setUrl(url);
            setWebhook.setCertificateFile(publicCertificatePath);

            final String responseContent = BotUtil.doPostJSONQuery(this, SetWebhook.PATH, setWebhook);
            final JSONObject jsonObject = new JSONObject(responseContent);
            if (!jsonObject.getBoolean(ApiConstants.RESPONSE_FIELD_OK)) {
                throw new TelegramApiRequestException("Error setting web hook", jsonObject);
            }
            LOGGER.info("Web hook set: {}", jsonObject);
        } catch (JSONException e) {
            throw new TelegramApiRequestException("Error de-serializing setWebHook method response", e);
        } catch (Exception e) {
            throw new TelegramApiRequestException("Error executing setWebHook method", e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotPath() {
        return config.getPath();
    }
}
