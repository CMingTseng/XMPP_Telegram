package xmpptelegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import sun.awt.AppContext;
import xmpptelegram.bot.TelegramBot;
import xmpptelegram.bot.XMPPBot;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class XmppTelegramApplication implements CommandLineRunner {

    @Autowired
    private XMPPBot xmppBot;

    @Autowired
    private TelegramBot telegramBot;

    public static void main(String[] args) {
        SpringApplication.run(XmppTelegramApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        telegramBot.init();
        xmppBot.start();
    }
}
