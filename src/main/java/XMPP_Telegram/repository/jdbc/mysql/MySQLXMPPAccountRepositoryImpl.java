package XMPP_Telegram.repository.jdbc.mysql;

import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.XMPPAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class MySQLXMPPAccountRepositoryImpl implements XMPPAccountRepository {
    private static final RowMapper<XMPPAccount> ROW_MAPPER = BeanPropertyRowMapper.newInstance(XMPPAccount.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MySQLXMPPAccountRepositoryImpl(@Qualifier("dataSource") DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<XMPPAccount> getAll() {
        return jdbcTemplate.query("SELECT * FROM xmpp_accounts", ROW_MAPPER);
    }

    @Override
    public XMPPAccount get(String server, String login) {

        return jdbcTemplate.queryForObject("SELECT * FROM xmpp_accounts WHERE server= ? AND login = ? LIMIT 1", ROW_MAPPER, server, login);
    }

    @Override
    public int delete(XMPPAccount account) {
        return jdbcTemplate.update("DELETE FROM xmpp_accounts WHERE login = ? AND server = ?", account.getLogin(), account.getServer());
    }

    @Override
    public int update(XMPPAccount account, String server, String login, String password, int port) {
        return jdbcTemplate.update("UPDATE xmpp_accounts SET server = ?, login = ?, password = ?, port = ? WHERE server = ? AND login = ?", server, login, password, port, account.getServer(), account.getLogin());
    }

    @Override
    public void create(String server, String login, String password, int port) {
        jdbcTemplate.update("INSERT INTO xmpp_accounts (login, password, server, port) VALUES (?, ?, ?, ?)", login, password, server, port);
    }

}
