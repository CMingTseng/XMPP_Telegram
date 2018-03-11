package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.repository.TelegramUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaTelegramUserRepositoryImpl implements TelegramUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TelegramUser> getAll() {
        return entityManager.createNamedQuery(TelegramUser.ALL, TelegramUser.class)
                .getResultList();
    }

    @Override
    public TelegramUser getById(int id) {
        return entityManager.createNamedQuery(TelegramUser.GET_BY_ID, TelegramUser.class)
                .setParameter("id", id)
                .getSingleResult();
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
    public void create(int id, String username) {

    }
}
