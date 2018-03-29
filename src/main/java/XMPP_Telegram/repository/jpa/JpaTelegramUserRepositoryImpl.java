package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.repository.TelegramUserRepository;
import XMPP_Telegram.service.TelegramWebHookService;
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
public class JpaTelegramUserRepositoryImpl implements TelegramUserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TelegramUser> getAll() {
        return entityManager.createNamedQuery(TelegramUser.ALL, TelegramUser.class)
                .getResultList();
    }

    @Override
    public TelegramUser getById(int id) {
        try {
            return entityManager.createNamedQuery(TelegramUser.GET_BY_ID, TelegramUser.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warn(String.format("User not found %d", id));
            return null;
        }
    }

    @Override
    public int delete(TelegramUser user) {
        return 0;
    }

    @Override
    public int update(TelegramUser user, String name) {
        return 0;
    }

    @Override
    @Transactional
    public void create(int id, String username) throws Exception {
        try {
            TelegramUser user = new TelegramUser(id, username);
            entityManager.persist(user);
        } catch (Exception e) {
            LOGGER.error(String.format("Ошибка заведения пользователя Telegram в БД! id: %d, username: %s", id, username), e);
            throw e;
        }
    }
}
