package xmpptelegram.repository.jpa;

import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import xmpptelegram.model.TransferMessage;
import xmpptelegram.model.UnsentMessage;
import xmpptelegram.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMessageRepositoryImpl implements MessageRepository {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JpaMessageRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(UnsentMessage message) {
        try {
            entityManager.persist(message);
        }catch (Exception e) {
            LOGGER.error(String.format("Error adding message! Message: %s",message.toString()), e.getMessage());
        }
    }

    @Override
    public int update(UnsentMessage message) {
        return 0;
    }

    @Override
    @Transactional
    public void delete(UnsentMessage message) {
        try {
            entityManager.remove(message);
        }catch (Exception e) {
            LOGGER.error(String.format("Error deleting message! Message: %s",message.toString()), e.getMessage());
        }
    }

    @Override
    public UnsentMessage get(int id) {
        return entityManager.find(UnsentMessage.class, id);
    }

    @Override
    public List<UnsentMessage> getAll() {
        try {
            return entityManager.createNamedQuery(UnsentMessage.GET_ALL, UnsentMessage.class)
                    .getResultList();
        }catch (NoResultException e) {
            return null;
        }
    }
}
