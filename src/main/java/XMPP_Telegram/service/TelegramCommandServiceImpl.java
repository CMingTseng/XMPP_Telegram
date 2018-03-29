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
        switch (command) {
            case "/start":
                return start(update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName());
            case "/addaccount": {
                String[] args = update.getMessage().getText().split(" ");
                try {
                    if (args.length == 4)
                        return addAccount(update.getMessage().getFrom().getId(), args[1], args[2], args[3]);
                    else if (args.length == 5)
                        return addAccount(update.getMessage().getFrom().getId(), args[1], args[2], args[3], Integer.parseInt(args[4]));
                    else return "Команда не распознана";
                } catch (Exception e) {
                    LOGGER.warn(String.format("Аргументы команды /addaccount не распознаны! Команда: %s", update.getMessage().getText()), e);
                    return "Команда не распознана";
                }
            }
            case "/addgroup": {
                String[] args = update.getMessage().getText().split(" ");
                try {
                    if (args.length == 3) {
                        TelegramUser user = telegramUserService.getById(update.getMessage().getFrom().getId());
                        if (user == null)
                            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
                        XMPPAccount account = xmppAccountService.get(args[1].split("@")[1], args[1].split("@")[0]);
                        if (account == null || !account.getTelegramUser().equals(user))
                            return "XMPP-аккаунт не зарегистрирован для данного пользователя. Добавьте аккаунт командой /addaccount";
                        if (addGroup(update.getMessage().getFrom().getId(), update.getMessage().getChat().getId(), account, args[2]))
                            return "Группа успешно добавлена";
                        else return "Ошибка заведения группы!";
                    } else return "Команда не распознана!";
                } catch (Exception e) {
                    LOGGER.warn("Аргументы команды /addgroup не распознаны!", e);
                    return "Команда не распознана!";
                }
            }
            default:
                return "Команда не распознана!";
        }
    }

    @Override
    public String start(int id, String login) {
        TelegramUser user = telegramUserService.getById(id);
        if (user == null) {
            if (telegramUserService.create(id, login)) {
                return "Пользователь успешно зарегистрирован";
            } else return "Ошибка создания нового пользователя!";
        } else return "Пользователь уже существует";
    }

    @Override
    public String addAccount(int userId, String server, String login, String password) {
        return addAccount(userId, server, login, password, 5222);
    }

    @Override
    public String addAccount(int userId, String server, String login, String password, int port) {
        if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if (xmppAccountService.get(server, login).getTelegramUser().getId() != userId) {
            return "Данный аккаунт уже зарегистрирован другим пользователем";
        } else if (xmppAccountService.get(server, login).getTelegramUser().getId() == userId) {
            return "Данный аккаунт уже Вами зарегистрирован";
        } else {
            if (xmppAccountService.create(telegramUserService.getById(userId), server, login, password, port)) {
                return "Аккаунт успешно добавлен";
            } else return "Ошибка добавления аккаунта!";
        }
    }

    @Override
    public String addGroup(int userId, long chatId, String server, String login, String contact) {
        if (telegramUserService.getById(userId)==null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        }else if (xmppAccountService.get(server, login) == null || xmppAccountService.get(server, login).getTelegramUser().getId()!=userId) {
            return "Данный XMPP-аккаунт Вами не зарегистрирован";
        } else {
            if (chatMapService.create(xmppAccountService.get(server, login), chatId, contact)){

            }
        }


        return map != null;
    }
}
