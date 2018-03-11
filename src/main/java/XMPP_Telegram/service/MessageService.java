package XMPP_Telegram.service;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;

public interface MessageService {
    void create(TransferMessage message);

    void messageFromXMPP(XMPPAccount account, String contact, String text);
}
