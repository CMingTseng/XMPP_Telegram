package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaMessageRerositoryImpl implements MessageRepository {
    @Override
    public List<TransferMessage> readNotSent(XMPPAccount account) {
        return null;
    }

    @Override
    public TransferMessage readById(XMPPAccount account, long id) {
        return null;
    }

    @Override
    public void create(TransferMessage message) {

    }

    @Override
    public int update(TransferMessage message) {
        return 0;
    }

    @Override
    public int delete(TransferMessage message) {
        return 0;
    }
}
