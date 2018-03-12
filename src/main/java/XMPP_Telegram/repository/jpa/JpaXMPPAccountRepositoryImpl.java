package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import XMPP_Telegram.service.TelegramWebHookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaXMPPAccountRepositoryImpl implements XMPPAccountRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramWebHookService.class);

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
        } catch (Exception e) {
            LOGGER.warn(String.format("User not found login: %s, server %s", login, server), e);
            return null;
        }
    }

    @Override
    public int delete(XMPPAccount account) {
        return 0;
    }

    @Override
    public int update(XMPPAccount account, String server, String login, String password, int port) {
        return 0;
    }

    @Override
    public void create(String server, String login, String password, int port) {

    }

    @Override
    public XMPPAccount getById(int id) {
        return entityManager.find(XMPPAccount.class, id);
    }
}
