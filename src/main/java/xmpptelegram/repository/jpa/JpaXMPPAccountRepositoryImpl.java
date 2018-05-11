package xmpptelegram.repository.jpa;

import xmpptelegram.model.XMPPAccount;
import xmpptelegram.repository.XMPPAccountRepository;
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
public class JpaXMPPAccountRepositoryImpl implements XMPPAccountRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaXMPPAccountRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<XMPPAccount> getAll() {
        return entityManager.createNamedQuery(XMPPAccount.ALL, XMPPAccount.class)
                .getResultList();
    }

    @Override
    public XMPPAccount get(String server, String login) {
        try {
            return entityManager.createNamedQuery(XMPPAccount.GET_BY_LOGIN_AND_SERVER, XMPPAccount.class)
                    .setParameter("server", server)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warn(String.format("User not found login: %s, server %s", login, server), e.getMessage());
            return null;
        }
    }

    @Override
    public int delete(XMPPAccount account) {
        return 0;
    }

    @Override
    @Transactional
    public XMPPAccount update(XMPPAccount account) {
        try {
            return entityManager.merge(account);
        }catch (Exception e) {
            LOGGER.warn(String.format("Error updating XMPP-account! Server: %s, login: %s",account.getServer(),account.getLogin()), e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void create(XMPPAccount account) {
        entityManager.persist(account);
    }

    @Override
    public XMPPAccount getById(int id) {
        return entityManager.find(XMPPAccount.class, id);
    }

    @Override
    public List<XMPPAccount> getAllByUser(int userId) {
        try {
            return entityManager.createNamedQuery(XMPPAccount.GET_ALL_BY_USER, XMPPAccount.class)
                    .setParameter("telegramUserId", userId)
                    .getResultList();
        }catch (NoResultException e) {
            LOGGER.warn(String.format("Users not found teelegram user id: %s",userId), e.getMessage());
            return null;
        }
    }
}
