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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaChatMapRepositoryImpl implements ChatMapRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    @PersistenceContext
    private EntityManager entityManager;

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
        } catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for XMPPAccount: %s, contact: %s", xmppAccount.getLogin() + "@" + xmppAccount.getServer(), contact), e);
            return null;
        }
    }

    @Override
    public ChatMap getByUserAndAccountAndContact(TelegramUser user, XMPPAccount account, String contact) {
        try {
            return entityManager.createNamedQuery(ChatMap.GET_BY_USER_ACCOUNT_CONTACT, ChatMap.class)
                    .setParameter("xmppAccount", account)
                    .setParameter("telegramUser", user)
                    .setParameter("contact", contact)
                    .getSingleResult();
        }catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for XMPPAccount: %s, TelegramUser: %s, contact: %s", account.getLogin() + "@" +account.getServer(), user.getId(), contact), e);
            return null;
        }
    }

    @Override
    @Transactional
    public void create(ChatMap chatMap) {
        entityManager.persist(chatMap);
    }

    @Override
    @Transactional
    public ChatMap update(ChatMap chatMap) {
        return entityManager.merge(chatMap);
    }

    @Override
    public ChatMap sendToXMPP(TelegramUser telegramUser, long chatId) {
        try {
            return entityManager.createNamedQuery(ChatMap.SEND_TO_XMPP, ChatMap.class)
                    .setParameter("telegramUser", telegramUser)
                    .setParameter("chatid", chatId)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for TelegramUser: %s, chatId: %s", telegramUser.getId(), chatId));
            return null;
        }
    }
}
