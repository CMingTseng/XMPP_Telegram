package XMPP_Telegram.repository;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;

import java.util.List;

public interface MessageRepository {

    List<TransferMessage> readNotSent(XMPPAccount account);

    TransferMessage readById (XMPPAccount account, long id);

    void create(TransferMessage message);

    int update (TransferMessage message);

    int delete (TransferMessage message);

}
