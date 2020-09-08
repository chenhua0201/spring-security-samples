package security302.auth.repository;

import java.util.List;

import security302.auth.entity.AuthRole;

/**
 * 角色仓库接口。
 */
public interface AuthRoleRepository {

	/**
	 * 查询账号的角色列表。
	 *
	 * @param accountId 账号ID
	 * @return 账号的角色列表
	 */
	List<AuthRole> findByAccountId(String accountId);

}
