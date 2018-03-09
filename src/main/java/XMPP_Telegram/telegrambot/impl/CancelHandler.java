package XMPP_Telegram.telegrambot.impl;

import XMPP_Telegram.service.TelegramUserService;
import XMPP_Telegram.telegrambot.general.CommandHandler;
import XMPP_Telegram.telegrambot.general.ICancelHandler;
import XMPP_Telegram.telegrambot.general.ICommandHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author UnAfraid
 */
@Service
public class CancelHandler implements ICommandHandler {

    @Inject
    private TelegramUserService usersService;

    @Override
    public String getCommand() {
        return "/cancel";
    }

    @Override
    public String getUsage() {
        return "/cancel";
    }

    @Override
    public String getDescription() {
        return "Cancels current action";
    }

    @Override
    public void onCommandMessage(AbsSender bot, Update update, Message message, List<String> args) throws TelegramApiException {
        for (ICancelHandler handler : CommandHandler.getInstance().getHandlers(ICancelHandler.class, message.getFrom().getId(), usersService)) {
            handler.onCancel(bot, update, message);
        }
    }
}
