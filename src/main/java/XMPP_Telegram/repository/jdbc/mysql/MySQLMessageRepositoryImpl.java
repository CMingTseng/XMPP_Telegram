package XMPP_Telegram.repository.jdbc.mysql;

import XMPP_Telegram.model.TransferMessage;
import XMPP_Telegram.model.XMPPAccount;
import XMPP_Telegram.repository.MessageRepository;
import XMPP_Telegram.repository.XMPPAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MySQLMessageRepositoryImpl implements MessageRepository {
    private static final RowMapper<TransferMessage> ROW_MAPPER = BeanPropertyRowMapper.newInstance(TransferMessage.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private XMPPAccountRepository accountRepository;

    @Autowired
    public MySQLMessageRepositoryImpl(@Qualifier("dataSource") DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
//        this.insertXMPPAccount = new SimpleJdbcInsert(dataSource)
//                .withTableName("xmpp_accounts")
//                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
//        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<TransferMessage> readNotSent(XMPPAccount account) {
        return null;
    }

    @Override
    public TransferMessage readById(XMPPAccount account, long id) {
        return null;
    }

    @Override
    public void create(TransferMessage message) {
        jdbcTemplate.update("INSERT INTO messages (text, xmppaccount, contact, fromXMPP, isSent, date) VALUES (?, ?, ?, ?, ?, ?)", message.getText(), accountRepository.get(message.getAccount().getServer(),message.getAccount().getLogin()).getId(), message.getContact(), message.isFromXMPP(), message.isSent(), message.getDate());
    }

    @Override
    public int update(TransferMessage message) {
        return 0;
    }

    @Override
    public int delete(TransferMessage message) {
        return 0;
    }

    public void setAccountRepository(XMPPAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
