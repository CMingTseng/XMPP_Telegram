DROP TABLE IF EXISTS unsent_messages;
DROP TABLE IF EXISTS telegram_chats;
DROP TABLE IF EXISTS xmpp_accounts;
DROP TABLE IF EXISTS telegram_users;

CREATE TABLE telegram_users
(
  id          INT NOT NULL PRIMARY KEY,
  username    VARCHAR(250),
  defaultchat INT
);

CREATE TABLE xmpp_accounts
(
  id           INT IDENTITY PRIMARY KEY,
  login        VARCHAR(250)  NOT NULL,
  password     VARCHAR(250)  NOT NULL,
  server       VARCHAR(250)  NOT NULL,
  port         INT DEFAULT 5222,
  telegramuser INT           NOT NULL,
  active       BIT DEFAULT 1 NOT NULL,
  FOREIGN KEY (telegramuser) REFERENCES telegram_users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE UNIQUE INDEX xmpp_accounts_login_server_index
  ON xmpp_accounts (login, server);

CREATE TABLE telegram_chats
(
  id          INT IDENTITY PRIMARY KEY,
  chatid      BIGINT       NOT NULL,
  xmppaccount INT          NOT NULL,
  xmppcontact VARCHAR(250) NOT NULL,
  FOREIGN KEY (xmppaccount) REFERENCES xmpp_accounts (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE UNIQUE INDEX telegram_chats_chatid_index
  ON telegram_chats (chatid);
CREATE UNIQUE INDEX telegram_chats_index
  ON telegram_chats (chatid, xmppaccount, xmppcontact);

CREATE TABLE unsent_messages
(
  id          BIGINT IDENTITY PRIMARY KEY,
  text        varchar(8000),
  xmppcontact VARCHAR(250)  NOT NULL,
  xmppaccount INT           NOT NULL,
  fromXMPP    BIT DEFAULT 0 NOT NULL,
  date        TIMESTAMP     ,
  FOREIGN KEY (xmppaccount) REFERENCES xmpp_accounts (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE INDEX messages_dtinput_index
  ON unsent_messages (date);

--insert into telegram_users (id, defaultchat) values (625605, 625605)
--insert into xmpp_accounts (login, password, server, telegramuser) values ('testjav', '12345678', 'ifox.pro', 625605)
--insert into xmpp_accounts (login, password, server, telegramuser) values ('testjava', '12345678', 'ifox.pro', 625605)

--DELETE FROM xmpp_accounts;
--DELETE FROM telegram_users;

