package XMPP_Telegram;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.service.XMPPAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

//@ContextConfiguration("/application.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootApplication
public class XmppTelegramApplicationTests {

	@Autowired
	private XMPPAccountService xmppAccountService;

//	@Before
//	public static void main(String[] args) {
//		SpringApplication.run(XmppTelegramApplication.class, args);
//	}

	@Test
	public void contextLoads() {
		for (XMPPAccount s : xmppAccountService.getAll()) {
			System.out.println(s.toString());
		};
	}

}
