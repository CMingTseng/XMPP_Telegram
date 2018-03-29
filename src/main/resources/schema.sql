DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS telegram_chats;
DROP TABLE IF EXISTS xmpp_accounts;
DROP TABLE IF EXISTS telegram_users;

CREATE TABLE telegram_users
(
  `id`          INT NOT NULL,
  `username`    VARCHAR(250),
  `defaultchat` INT,
  PRIMARY KEY (`id`)
);

CREATE TABLE xmpp_accounts
(
  `id`           INT                  NOT NULL AUTO_INCREMENT,
  `login`        VARCHAR(250)         NOT NULL,
  `password`     VARCHAR(250)         NOT NULL,
  `server`       VARCHAR(250)         NOT NULL,
  `port`         INT                           DEFAULT 5222,
  `telegramuser` INT                  NOT NULL,
  `savehistory`  TINYINT(1) DEFAULT 1 NOT NULL,
  `active`       TINYINT(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (telegramuser) REFERENCES telegram_users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE UNIQUE INDEX xmpp_accounts_login_server_index
  ON xmpp_accounts (login, server);

CREATE TABLE telegram_chats
(
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `chatid`      BIGINT       NOT NULL,
  `xmppaccount` INT          NOT NULL,
  `xmppcontact` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (xmppaccount) REFERENCES xmpp_accounts (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE UNIQUE INDEX telegram_chats_chatid_index
  ON telegram_chats (chatid);
CREATE UNIQUE INDEX telegram_chats_index
  ON telegram_chats (chatid, xmppaccount, xmppcontact);

CREATE TABLE messages
(
  `id`       BIGINT               NOT NULL AUTO_INCREMENT,
  `text`     TEXT,
  `chatmap`  INT                  NOT NULL,
  `fromXMPP` TINYINT(1) DEFAULT 0 NOT NULL,
  `date`     TIMESTAMP            NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (chatmap) REFERENCES telegram_chats (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE INDEX messages_dtinput_index
  ON messages (date);

#
# DELETE FROM xmpp_accounts;
# DELETE FROM telegram_users;


# INSERT INTO telegram_users (id, username) VALUES (625605,'Lezenford');
# INSERT INTO xmpp_accounts (login, password, server, savehistory, telegramuser) VALUES ('support812', 'Non329nv', 'ifox.pro',1,625605);
# INSERT INTO telegram_chats (chatid, telegramuser, xmppaccount, xmppcontact) VALUES ('-301274558',625605,1,'support812@ifox.pro')
# INSERT INTO xmpp_accounts (login, password, server) VALUES ('testjava2', '12345678', 'ifox.pro');