package XMPP_Telegram.service;

import XMPP_Telegram.model.ChatMap;

import java.util.List;

public interface ChatMapService {
    List<ChatMap> getAll();
}
