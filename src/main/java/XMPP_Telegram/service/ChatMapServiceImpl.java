package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.repository.ChatMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMapServiceImpl implements ChatMapService {

    @Autowired
    private ChatMapRepository repository;

    public void setRepository(ChatMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChatMap> getAll() {
        return repository.getAll();
    }
}
