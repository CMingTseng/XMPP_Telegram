package XMPP_Telegram.repository.jdbc.mysql;

import XMPP_Telegram.model.ChatMap;
import XMPP_Telegram.repository.ChatMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MySQLChatMapRepositoryImpl implements ChatMapRepository {
    private static final RowMapper<ChatMap> ROW_MAPPER = BeanPropertyRowMapper.newInstance(ChatMap.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MySQLChatMapRepositoryImpl(@Qualifier("dataSource") DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<ChatMap> getAll() {
        return jdbcTemplate.query("SELECT * FROM telegram_chats", ROW_MAPPER);
    }
}
