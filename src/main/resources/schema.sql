DELETE FROM xmpp_accounts;
DELETE FROM telegram_users;


INSERT INTO telegram_users (id, username) VALUES (1,'123');
INSERT INTO xmpp_accounts (login, password, server, savehistory, telegramuser) VALUES ('testjava', '12345678', 'ifox.pro',1,1);
# INSERT INTO xmpp_accounts (login, password, server) VALUES ('testjava2', '12345678', 'ifox.pro');