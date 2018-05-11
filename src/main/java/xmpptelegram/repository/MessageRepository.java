package xmpptelegram.repository;

import xmpptelegram.model.TransferMessage;
import xmpptelegram.model.UnsentMessage;
import xmpptelegram.model.XMPPAccount;

import java.util.List;

public interface MessageRepository {

    void create(UnsentMessage message);

    int update (UnsentMessage message);

    void delete (UnsentMessage message);

    UnsentMessage get(int id);

    List<UnsentMessage> getAll();

}
