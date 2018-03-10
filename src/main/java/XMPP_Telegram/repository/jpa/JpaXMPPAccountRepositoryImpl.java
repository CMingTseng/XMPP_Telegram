package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaXMPPAccountRepositoryImpl implements XMPPAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<XMPPAccount> getAll() {
        return null;
    }

    @Override
    public XMPPAccount get(String server, String login) {
        return null;
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
}
