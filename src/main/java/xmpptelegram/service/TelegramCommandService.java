package xmpptelegram.service;

import xmpptelegram.bot.XMPPBot;
import xmpptelegram.model.TelegramUser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;
import xmpptelegram.model.XMPPAccount;

import java.util.List;

@Service
public class TelegramCommandService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TelegramCommandService.class);

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private XMPPAccountService xmppAccountService;

    @Autowired
    private ChatMapService chatMapService;

    @Autowired
    private XMPPBot xmppBot;

    public String useCommand(Update update) {
        String command = update.getMessage().getText().split(" ")[0].toLowerCase();
        String[] args = update.getMessage().getText().split(" ");
        switch (command) {
            case "/help": {
                return help();
            }
            case "/start": {
                if ((long) update.getMessage().getFrom().getId() == update.getMessage().getChatId()) {
                    return start(update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName());
                } else return "Команда не распознана";
            }
            case "/default": {
                return defaultChat(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            }
            case "/addaccount": {
                try {
                    if (args.length == 3)
                        return addAccount(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2]);
                    else if (args.length == 4)
                        return addAccount(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2], args[3]);
                    else if (args.length == 5)
                        return addAccount(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2], args[3], Integer.parseInt(args[4]));
                    else return "Команда не распознана";
                } catch (Exception e) {
                    LOGGER.warn(String.format("Аргументы команды /addaccount не распознаны! Команда: %s", update.getMessage().getText()), e);
                    return "Команда не распознана";
                }
            }
            case "/addgroup": {
                try {
                    if (args.length == 3) {
                        String server = args[1].split("@")[1];
                        String login = args[1].split("@")[0];
                        return addGroup(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), server, login, args[2]);
                    } else return "Команда не распознана!";
                } catch (Exception e) {
                    LOGGER.warn("Аргументы команды /addgroup не распознаны!", e);
                    return "Команда не распознана!";
                }
            }
            case "/deleteme": {
                if ((long) update.getMessage().getFrom().getId() == update.getMessage().getChatId()) {
                    return deleteTelegramAccount(update.getMessage().getFrom().getId());
                } else return "Команда не распознана!";
            }
            case "/deletegroup": {
                return deleteGroup(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            }
            case "/status": {
                return status(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            }
            case "/update": {
                try {
                    if (args.length == 3) {
                        return update(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2]);
                    } else if (args.length == 4) {
                        return update(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2], args[3]);
                    } else if (args.length == 5) {
                        return update(update.getMessage().getFrom().getId(), update.getMessage().getChatId(), args[1], args[2], args[3], Integer.parseInt(args[4]));
                    } else return "Команда не распознана";
                } catch (Exception e) {
                    LOGGER.warn("Аргументы команды /update не распознаны!", e);
                    return "Команда не распознана!";
                }
            }
            default:
                return "Команда не распознана!";
        }
    }

    private String help() {
        return "/start - регистрация нового пользователя\n" +
                "/default - выбор чата по-умолчанию для всех сообщений\n" +
                "/addAccount [XMPP-account] [password] - заведение нового XMPP-аккаунта\n" +
                "/addAccount [XMPP-server] [XMPP-account] [password] - заведение нового XMPP-аккаунта\n" +
                "/addAccount [XMPP-server] [XMPP-account] [password] [port] - заведение нового XMPP-аккаунта\n" +
                "/update [XMPP-account] [password] - обновление данных XMPP-аккаунта\n" +
                "/update [XMPP-server] [XMPP-account] [password] - обновление данных XMPP-аккаунта\n" +
                "/update [XMPP-server] [XMPP-account] [password] [port] - обновление данных XMPP-аккаунта\n" +
                "/addGroup [XMPP-account] [XMPP-contact] - переадресация в текущую группу сообщений данного XMPP-contact (работает только в групповых чатах)\n" +
                "/addGroup [XMPP-account] [XMPP-contact] - переадресация в текущую группу сообщений данного XMPP-contact (работает только в групповых чатах)\n" +
                "/deleteGroup - отключение переадресации в текущую группу\n" +
                "/deleteMe - отключение от бота\n";
    }

    private String start(int id, String login) {
        TelegramUser user = telegramUserService.getById(id);
        if (user == null) {
            if (telegramUserService.create(id, login)) {
                return "Пользователь успешно зарегистрирован";
            } else return "Ошибка создания нового пользователя!";
        } else return "Пользователь уже существует";
    }

    private String addAccount(int userId, long chatId, String login, String password) throws Exception {
        String server = login.split("@")[1];
        login = login.split("@")[0];
        return addAccount(userId, chatId, server, login, password, 5222);
    }

    private String addAccount(int userId, long chatId, String server, String login, String password) {
        return addAccount(userId, chatId, server, login, password, 5222);
    }

    private String addAccount(int userId, long chatId, String server, String login, String password, int port) {
        if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if ((long) userId != chatId) {
            return "Данную команду нельзя использовать в групповом чате";
        } else if (xmppAccountService.get(server, login) != null) {
            if (xmppAccountService.get(server, login).getTelegramUser().getId() == userId) {
                return "Данный аккаунт уже Вами зарегистрирован";
            } else {
                return "Данный аккаунт уже зарегистрирован другим пользователем";
            }
        } else {
            if (xmppAccountService.create(telegramUserService.getById(userId), server, login, password, port)) {
                xmppBot.connectAccount(xmppAccountService.get(server, login));
                return "Аккаунт успешно добавлен";
            } else return "Ошибка добавления аккаунта!";
        }
    }

    private String addGroup(int userId, long chatId, String server, String login, String contact) {
        if ((long) userId == chatId) {
            return "Чат не является группой";
        } else if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if (xmppAccountService.get(server, login) == null || xmppAccountService.get(server, login).getTelegramUser().getId() != userId) {
            return "Данный XMPP-аккаунт Вами не зарегистрирован";
        } else {
            if (chatMapService.create(xmppAccountService.get(server, login), chatId, contact)) {
                return "Группа успешно создана";
            } else return "Ошибка добавления группы!";
        }
    }

    private String deleteTelegramAccount(int id) {
        if (telegramUserService.getById(id) == null) {
            return "Пользователь не существует";
        } else {
            if (telegramUserService.delete(id)) {
                return "Пользователь удален";
            } else return "Ошибка удаления пользователя!";
        }
    }

    private String deleteGroup(int userId, long chatId) {
        if ((long) userId == chatId) {
            return "Чат не является группой";
        } else if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if (chatMapService.getByChatId(chatId) == null) {
            return "Группа не существует";
        } else if (chatMapService.getByChatId(chatId).getXmppAccount().getTelegramUser().getId() != userId) {
            return "У Вас нет прав на управление данной группой";
        } else if (chatMapService.delete(chatId)) {
            return "Группа успешно удалена";
        } else return "Ошибка удаления группы!";
    }

    private String defaultChat(int userId, long chatId) {
        if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if (telegramUserService.getById(userId).getDefaultChat() == chatId) {
            return "Этот чат уже является чатом по-умолчанию";
        } else {
            TelegramUser user = telegramUserService.getById(userId);
            user.setDefaultChat(chatId);
            if (telegramUserService.update(user) != null) {
                return "Чат по-умолчанию переопределен";
            } else return "Ошибка переопределения чата по-умолчанию!";
        }
    }

    private String status(int userId, long chatId) {
        if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if ((long) userId != chatId) {
            return "Данную команду нельзя использовать в групповом чате";
        } else if (xmppAccountService.getAllByUser(telegramUserService.getById(userId)).size() == 0) {
            return "Нет активных XMPP-аккаунтов";
        } else {
            List<XMPPAccount> accounts = xmppAccountService.getAllByUser(telegramUserService.getById(userId));
            StringBuilder result = new StringBuilder();
            for (XMPPAccount account : accounts) {
                result.append(String.format("Аккаунт %s: %s\n", account.getLogin() + "@" + account.getServer(), xmppBot.checkStatus(account)));
            }
            return result.toString();
        }
    }

    private String update(int userId, long chatId, String login, String password) throws Exception {
        String server = login.split("@")[1];
        login = login.split("@")[0];
        return update(userId, chatId, server, login, password, 5222);
    }

    private String update(int userId, long chatId, String server, String login, String password) {
        return update(userId, chatId, server, login, password, 5222);
    }

    private String update(int userId, long chatId, String server, String login, String password, int port) {
        if (telegramUserService.getById(userId) == null) {
            return "Telegram-аккаунт не зарегистрирован! Выполните команду /start";
        } else if ((long) userId != chatId) {
            return "Данную команду нельзя использовать в групповом чате";
        } else if (xmppAccountService.get(server, login) == null) {
            return "Данный XMPP-аккаунт не зарегистрирован";
        } else {
            XMPPAccount account = xmppAccountService.get(server, login);
            account.setPassword(password);
            account.setPort(port);
            if (xmppAccountService.update(account) == null) {
                return "Ошибка обновления аккаунта!";
            } else return "Данные успешно обновлены";
        }
    }
}