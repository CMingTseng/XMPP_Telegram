package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.util.BotUtil;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.methods.stickers.AddStickerToSet;
import org.telegram.telegrambots.api.methods.stickers.CreateNewStickerSet;
import org.telegram.telegrambots.api.methods.stickers.UploadStickerFile;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.WebhookBot;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import java.io.Serializable;
import java.util.List;

public abstract class TelegramWebHookService extends AbsSender implements WebhookBot {

    @Override
    protected <T extends Serializable, Method extends BotApiMethod<T>> T sendApiMethod(Method method) throws TelegramApiException {
        method.validate();

        try {
            final String result = BotUtil.doPostJSONQuery(this, method.getMethod(), method);
            return method.deserializeResponse(result);
        } catch (Exception e) {
            throw new TelegramApiException("Unable to execute " + method.getMethod() + " method", e);
        }
    }

//    Don't use async method
    @Override
    protected <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>> void sendApiMethodAsync(Method method, Callback callback) {

    }

    public abstract void sendToTelegram(ChatMap map, String text);

    @Override
    public Boolean setChatPhoto(SetChatPhoto setChatPhoto) throws TelegramApiException {
        return null;
    }

    @Override
    public Boolean addStickerToSet(AddStickerToSet addStickerToSet) throws TelegramApiException {
        return null;
    }

    @Override
    public Boolean createNewStickerSet(CreateNewStickerSet createNewStickerSet) throws TelegramApiException {
        return null;
    }

    @Override
    public File uploadStickerFile(UploadStickerFile uploadStickerFile) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendDocument(SendDocument sendDocument) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendPhoto(SendPhoto sendPhoto) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendVideo(SendVideo sendVideo) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendVideoNote(SendVideoNote sendVideoNote) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendSticker(SendSticker sendSticker) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendAudio(SendAudio sendAudio) throws TelegramApiException {
        return null;
    }

    @Override
    public Message sendVoice(SendVoice sendVoice) throws TelegramApiException {
        return null;
    }

    @Override
    public List<Message> sendMediaGroup(SendMediaGroup sendMediaGroup) throws TelegramApiException {
        return null;
    }



}
