package XMPP_Telegram.repository;

import XMPP_Telegram.model.ChatMap;

import java.util.List;

public interface ChatMapRepository {
    List<ChatMap> getAll();
}
