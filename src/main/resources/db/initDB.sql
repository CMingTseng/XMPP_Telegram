DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS telegram_chats;
DROP TABLE IF EXISTS xmpp_accounts;
DROP TABLE IF EXISTS telegram_users;

CREATE TABLE telegram_users
(
  `id`       INT NOT NULL,
  `username` VARCHAR(250),
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

CREATE TABLE messages
(
  `id`          BIGINT               NOT NULL AUTO_INCREMENT,
  `text`        TEXT,
  `xmppaccount` INT                  NOT NULL,
  `contact`     VARCHAR(250)         NOT NULL,
  `fromXMPP`    TINYINT(1) DEFAULT 0 NOT NULL,
  `isSent`      TINYINT(1) DEFAULT 0 NOT NULL,
  `date`        TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (xmppaccount) REFERENCES xmpp_accounts (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE INDEX messages_xmppaccount_index
  ON messages (xmppaccount);
CREATE INDEX messages_xmppaccount_contact_index
  ON messages (xmppaccount, contact);
CREATE INDEX messages_xmppaccount_contact_dtinput_index
  ON messages (xmppaccount, contact, date);
CREATE INDEX messages_issended_index
  ON messages (isSent);
CREATE INDEX messages_dtinput_index
  ON messages (date);
CREATE INDEX messages_issended_dtinput
  ON messages (isSent, date);

CREATE TABLE telegram_chats
(
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `chatid`       BIGINT       NOT NULL,
  `telegramuser` INT          NOT NULL,
  `xmppaccount`  INT          NOT NULL,
  `xmppcontact`  VARCHAR(250) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (telegramuser) REFERENCES telegram_users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE,
  FOREIGN KEY (xmppaccount) REFERENCES xmpp_accounts (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
);
CREATE UNIQUE INDEX telegram_chats_chatid_index
  ON telegram_chats (chatid);
CREATE INDEX telegram_chats_index
  ON telegram_chats (chatid, telegramuser, xmppaccount, xmppcontact);

