package security302.auth.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import security302.auth.entity.AuthAccount;
import security302.auth.repository.AuthAccountRepository;

/**
 * 账号仓库实现。
 */
@Repository
public class AuthAccountRepositoryImpl implements AuthAccountRepository {

	private final RowMapper<AuthAccount> authAccountMapper = (rs, rowNum) -> {
		int index = 1;

		final AuthAccount account = new AuthAccount();
		account.setId(rs.getString(index++));
		account.setUsername(rs.getString(index++));
		account.setPassword(rs.getString(index++));
		account.setEnabled(rs.getBoolean(index++));
		account.setDeleted(rs.getBoolean(index++));

		return account;
	};

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthAccountRepositoryImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public AuthAccount findByUsername(String username) {
		final String sql = "SELECT id,username,password,enabled,deleted FROM auth_account WHERE username=? AND deleted=FALSE";

		final List<AuthAccount> accounts = jdbcTemplate.query(sql, new Object[] { username }, authAccountMapper);

		return accounts == null || accounts.isEmpty() ? null : accounts.get(0);
	}

}
