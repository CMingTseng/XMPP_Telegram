package xmpptelegram.repository.jpa;

import xmpptelegram.model.ChatMap;
import xmpptelegram.model.XMPPAccount;
import xmpptelegram.repository.ChatMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaChatMapRepositoryImpl implements ChatMapRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaChatMapRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatMap> getAll() {
        return null;
    }

    @Override
    public ChatMap getByUserAndAccountAndContact(XMPPAccount account, String contact) {
        try {
            return entityManager.createNamedQuery(ChatMap.GET_BY_ACCOUNT_CONTACT, ChatMap.class)
                    .setParameter("xmppAccount", account)
                    .setParameter("xmppContact", contact)
                    .getSingleResult();
        }catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for XMPPAccount: %s, contact: %s", account.getLogin() + "@" +account.getServer(), contact), e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void create(ChatMap chatMap) throws Exception {
        try {
            entityManager.persist(chatMap);
        }catch (Exception e) {
            LOGGER.error(String.format("Error adding chatmap! Data: %s", chatMap.toString()), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public ChatMap update(ChatMap chatMap) {
        return entityManager.merge(chatMap);
    }

    @Override
    public ChatMap getByChatId(long chatId) {
        try {
            return entityManager.createNamedQuery(ChatMap.GET_BY_CHATID, ChatMap.class)
                    .setParameter("chatId", chatId)
                    .getSingleResult();
        }catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for chatId: %d", chatId), e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(ChatMap chatMap) {
        try {
            entityManager.remove(chatMap);
        }catch (Exception e) {
            LOGGER.warn(String.format("Error deleting chatmap: %s", chatMap.toString()), e.getMessage());
        }
    }

    @Override
    public ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact) {
        try {
            return entityManager.createNamedQuery(ChatMap.GET_BY_ACCOUNT_CONTACT, ChatMap.class)
                    .setParameter("xmppAccount", xmppAccount)
                    .setParameter("xmppContact", contact)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warn(String.format("Empty chatmap data for XMPPAccount: %s, contact: %s", xmppAccount.getLogin() + "@" + xmppAccount.getServer(), contact), e.getMessage());
            return null;
        }
    }
}
