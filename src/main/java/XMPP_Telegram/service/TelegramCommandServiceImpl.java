package XMPP_Telegram.service;

import XMPP_Telegram.controller.XMPPController;
import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

@Service
public class TelegramCommandServiceImpl implements TelegramCommandService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private XMPPAccountService xmppAccountService;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private XMPPController controller;

    @Override
    public String useCommand(Update update) {
        String command = update.getMessage().getText().split(" ")[0].toLowerCase();
        if (command.equals("/start")) {
            start(update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName());
            return "Пользователь зарегистрирован";
        } else if (command.equals("/addaccount")) {
            String[] args = update.getMessage().getText().split(" ");
            try {
                if (args.length == 4)
                    if (addAccount(update.getMessage().getFrom().getId(), args[1], args[2], args[3])) {
                        return "Аккаунт успешно добавлен";
                    } else return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
                else if (args.length == 5)
                    if (addAccount(update.getMessage().getFrom().getId(), args[1], args[2], args[3], Integer.parseInt(args[4])))
                        return "Аккаунт успешно добавлен";
                    else return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
                else return "Команда не распознана";
            } catch (Exception e) {
                LOGGER.warn("Аргументы команды /addaccount не распознаны!", e);
                return "Команда не распознана";
            }
        } else if (command.equals("/addgroup")) {
            String[] args = update.getMessage().getText().split(" ");
            try {
                if (args.length == 3) {
                    TelegramUser user = telegramUserService.getById(update.getMessage().getFrom().getId());
                    if (user == null)
                        return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
                    XMPPAccount account = xmppAccountService.get(args[1].split("@")[1], args[1].split("@")[0]);
                    if (account == null)
                        return "XMPP-аккаунт не зарегистрирован для данного пользователя. Добавьте аккаунт командой /addaccount";
                    if (addGroup(user, update.getMessage().getChat().getId(), account, args[2]))
                        return "Группа успешно добавлена";
                    else return "Ошибка заведения группы!";
                } else return "Команда не распознана!";
            } catch (Exception e) {
                LOGGER.warn("Аргументы команды /addgroup не распознаны!", e);
                return "Команда не распознана";
            }
        }
        return "Команда не распознана!";
    }

    @Override
    public void start(int id, String login) {
        telegramUserService.create(id, login);
    }

    @Override
    public boolean addAccount(int userId, String server, String login, String password) {
        return addAccount(userId, server, login, password, 5222);
    }

    @Override
    public boolean addAccount(int userId, String server, String login, String password, int port) {
        if (telegramUserService.getById(userId) == null) {
            return false;
        }
        XMPPAccount account = xmppAccountService.create(telegramUserService.getById(userId), server, login, password, port);
        if (account != null)
            controller.connectAccount(account);
        return account != null;
    }

    @Override
    public boolean addGroup(TelegramUser user, long chatId, XMPPAccount account, String contact) {
        ChatMap map = chatMapService.create(user, account, chatId, contact);
        return map != null;
    }
}
