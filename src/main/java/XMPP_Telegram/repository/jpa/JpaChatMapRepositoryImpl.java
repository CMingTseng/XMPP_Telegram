package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.ChatMapRepository;
import XMPP_Telegram.service.TelegramWebHookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaChatMapRepositoryImpl implements ChatMapRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ChatMap> getAll() {
        return null;
    }

    @Override
    public ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact) {
        try {
            return entityManager.createNamedQuery(ChatMap.SEND_TO_Telegram, ChatMap.class)
                    .setParameter("xmppAccount", xmppAccount)
                    .setParameter("xmppContact", contact)
                    .getSingleResult();
        } catch (Exception e) {
            LOGGER.warn(String.format("Empty chatmap data for XMPPAccount: %s, contact: %s", xmppAccount.getLogin() + "@" + xmppAccount.getServer(), contact), e);
            return null;
        }
    }

    @Override
    public ChatMap sendToXMPP(TelegramUser telegramUser, long chatId) {
        try {
        return entityManager.createNamedQuery(ChatMap.SEND_TO_XMPP, ChatMap.class)
                .setParameter("telegramUser", telegramUser)
                .setParameter("chatid", chatId)
                .getSingleResult();
        } catch (Exception e) {
            LOGGER.warn(String.format("Empty chatmap data for TelegramUser: %s, chatId: %s", telegramUser.getId(), chatId), e);
            return null;
        }
    }
}
