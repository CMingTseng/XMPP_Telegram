package XMPP_Telegram.service;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

@Service
public class TelegramCommandServiceImpl implements TelegramCommandService {
    @Autowired
    private TelegramUserService telegramUserService;

    @Override
    public String useCommand(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        if (command.equals("/start")) {
            start(update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName());
            return "Пользователь зарегистрирован";
        } else if (command.equals("/addgroup")) {

        }
        return "Команда не распознана!";
    }

    @Override
    public void start(int id, String login) {
        telegramUserService.create(id, login);
    }

    @Override
    public void addGroup(TelegramUser user, long chatId, XMPPAccount account, String contact) {

    }


}
