package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.repository.ChatMapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaChatMapRepositoryImpl implements ChatMapRepository {
    @Override
    public List<ChatMap> getAll() {
        return null;
    }
}
