package XMPP_Telegram.repository.jpa;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.ChatMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class JpaChatMapRepositoryImpl implements ChatMapRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<ChatMap> getAll() {
        return null;
    }

    @Override
    public ChatMap sendToTelegram(XMPPAccount xmppAccount, String contact) {
        return entityManager.createNamedQuery(ChatMap.SEND_TO_Telegram, ChatMap.class)
                .setParameter("xmppAccount", xmppAccount)
                .setParameter("xmppContact", contact)
                .getSingleResult();
    }

    @Override
    public ChatMap sendToXMPP(TelegramUser telegramUser, long chatId) {
        return entityManager.createNamedQuery(ChatMap.SEND_TO_XMPP, ChatMap.class)
                .setParameter("telegramUser", telegramUser)
                .setParameter("chatid", chatId)
                .getSingleResult();
    }
}
