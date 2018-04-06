package xmpptelegram.repository.jpa;

import xmpptelegram.model.TelegramUser;
import xmpptelegram.repository.TelegramUserRepository;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaTelegramUserRepositoryImpl.class);

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
            LOGGER.warn(String.format("User not found %d", id),e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(TelegramUser user) throws Exception {
        try {
            entityManager.remove(user);
        } catch (Exception e) {
            LOGGER.error(String.format("Error deleting Telegram-account! id: %s", user.getId()), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void update(TelegramUser user) throws Exception {
        try {
            entityManager.merge(user);
        }catch (Exception e) {
            LOGGER.error(String.format("Error updating Telegram-account! id: %d, username: %s", user.getId(), user.getName()), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void create(int id, String username) throws Exception {
        try {
            TelegramUser user = new TelegramUser(id, username);
            entityManager.persist(user);
        } catch (Exception e) {
            LOGGER.error(String.format("Error adding Telegram-account! id: %d, username: %s", id, username), e.getMessage());
            throw e;
        }
    }
}
