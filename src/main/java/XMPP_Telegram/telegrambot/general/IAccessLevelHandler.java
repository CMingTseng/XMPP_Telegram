package XMPP_Telegram.telegrambot.general;

import XMPP_Telegram.service.TelegramUserService;

/**
 * @author UnAfraid
 */
public interface IAccessLevelHandler {
    /**
     * @return The access level required to execute this command
     */
    default int getRequiredAccessLevel() {
        return 0;
    }

    static boolean validate(Object handler, int id, TelegramUserService usersService) {
        if (handler instanceof IAccessLevelHandler) {
            return usersService.validate(id, ((IAccessLevelHandler) handler).getRequiredAccessLevel());
        }
        return true;
    }
}
