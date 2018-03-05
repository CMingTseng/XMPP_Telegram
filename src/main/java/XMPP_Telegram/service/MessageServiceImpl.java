package XMPP_Telegram.service;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository repository;

    @Override
    public void create(TransferMessage message) {
        repository.create(message);
    }

    public void setRepository(MessageRepository repository) {
        this.repository = repository;
    }
}
