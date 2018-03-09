package XMPP_Telegram;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import XMPP_Telegram.service.XMPPAccountService;
import XMPP_Telegram.telegrambot.general.CommandHandler;
import XMPP_Telegram.telegrambot.general.ICommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class XmppTelegramApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmppTelegramApplication.class, args);
	}
}
