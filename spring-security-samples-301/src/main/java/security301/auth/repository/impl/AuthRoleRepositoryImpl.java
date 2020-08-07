package security301.auth.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import security301.auth.entity.AuthRole;
import security301.auth.repository.AuthRoleRepository;

/**
 * 角色仓库实现。
 */
@Repository
public class AuthRoleRepositoryImpl implements AuthRoleRepository {

	private final RowMapper<AuthRole> authRoleMapper = (rs, rowNum) -> {
		int index = 1;

		final AuthRole role = new AuthRole();
		role.setId(rs.getString(index++));
		role.setName(rs.getString(index++));
		role.setDescription(rs.getString(index++));

		return role;
	};

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthRoleRepositoryImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<AuthRole> findByAccountId(String accountId) {
		final String sql = "SELECT r.id, r.name, r.description FROM auth_role AS r INNER JOIN auth_account_role AS j ON r.id = j.role_id WHERE j.account_id = ?";

		return jdbcTemplate.query(sql, new Object[] { accountId }, authRoleMapper);
	}

}
