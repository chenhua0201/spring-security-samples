package security304.auth.repository;

import java.util.Collection;
import java.util.List;

import security304.auth.entity.AuthRole;

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

	/**
	 * 查询权限所属的角色列表。
	 *
	 * @param permissionIds 权限ID列表
	 * @return 权限所属的角色列表
	 */
	List<AuthRole> findByPermissionIds(Collection<String> permissionIds);

	/**
	 * 查询超级角色列表。
	 *
	 * @return 超级角色列表
	 */
	List<AuthRole> findSuper();
}
