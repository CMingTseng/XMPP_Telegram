package XMPP_Telegram.repository.jdbc.mysql;

import XMPP_Telegram.model.TelegramUser;
import XMPP_Telegram.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MySQLTelegramUserRepositoryImpl implements TelegramUserRepository {
    private static final RowMapper<TelegramUser> ROW_MAPPER = BeanPropertyRowMapper.newInstance(TelegramUser.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MySQLTelegramUserRepositoryImpl(@Qualifier("dataSource") DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TelegramUser> getAll() {
        return jdbcTemplate.query("SELECT * FROM telegram_users", ROW_MAPPER);
    }

    @Override
    public TelegramUser getByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM telegram_users WHERE username = ?", ROW_MAPPER, name);
    }

    @Override
    public TelegramUser getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM telegram_users WHERE id = ?", ROW_MAPPER, id);
    }

    @Override
    public int delete(TelegramUser user) {
        return jdbcTemplate.update("DELETE FROM telegram_users WHERE id = ?", user.getId());
    }

    @Override
    public int update(TelegramUser user, String name) {
        return jdbcTemplate.update("UPDATE telegram_users SET username = ? WHERE id = ?", name, user.getId());
    }

    @Override
    public void create(int id, String username) {
        jdbcTemplate.update("INSERT INTO telegram_users (id, username) VALUES (?, ?)", id, username);
    }
}
