package security301.auth.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import security301.auth.entity.AuthAccount;
import security301.auth.repository.AuthAccountRepository;

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

		return account;
	};

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthAccountRepositoryImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public AuthAccount findByUsername(final String username) {
		final String sql = "SELECT id,username,password FROM auth_account WHERE username=?";

		final List<AuthAccount> accounts = jdbcTemplate.query(sql, authAccountMapper, username);

		return accounts == null || accounts.isEmpty() ? null : accounts.get(0);
	}

}
