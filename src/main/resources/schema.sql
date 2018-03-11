DELETE FROM xmpp_accounts;
DELETE FROM telegram_users;


INSERT INTO telegram_users (id, username) VALUES (625605,'Lezenford');
INSERT INTO xmpp_accounts (login, password, server, savehistory, telegramuser) VALUES ('testjava', '12345678', 'ifox.pro',1,625605);
INSERT INTO telegram_chats (chatid, telegramuser, xmppaccount, xmppcontact) VALUES ('-301274558',625605,3,'support812')
# INSERT INTO xmpp_accounts (login, password, server) VALUES ('testjava2', '12345678', 'ifox.pro');