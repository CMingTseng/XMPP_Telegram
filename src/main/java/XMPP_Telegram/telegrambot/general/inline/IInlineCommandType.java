package XMPP_Telegram.telegrambot.general.inline;

import java.util.List;

/**
 * Created by UnAfraid on 30.10.2016 г..
 */
public interface IInlineCommandType {
    String getName();

    int getRow();

    List<IInlineCommandType> getSubCommands();
}
