package XMPP_Telegram.service;

import XMPP_Telegram.model.TransferMessage;

public interface MessageService {
    void create(TransferMessage message);
}
