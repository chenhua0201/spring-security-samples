package security303.auth.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import security303.auth.entity.AuthPermission;
import security303.auth.repository.AuthPermissionRepository;

/**
 * 权限仓库实现。
 */
@Repository
public class AuthPermissionRepositoryImpl implements AuthPermissionRepository {

	private final RowMapper<AuthPermission> authPermissionMapper = (rs, rowNum) -> {
		int index = 1;

		final AuthPermission permission = new AuthPermission();
		permission.setId(rs.getString(index++));
		permission.setName(rs.getString(index++));
		permission.setUrl(rs.getString(index++));
		permission.setMethod(rs.getString(index++));

		return permission;
	};

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthPermissionRepositoryImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<AuthPermission> findAll() {
		final String sql = "SELECT id,name,url,method FROM auth_permission";

		return jdbcTemplate.query(sql, authPermissionMapper);
	}

}
