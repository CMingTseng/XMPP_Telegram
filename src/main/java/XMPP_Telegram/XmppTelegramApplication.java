package XMPP_Telegram;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import XMPP_Telegram.service.XMPPAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XmppTelegramApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmppTelegramApplication.class, args);
	}
}
