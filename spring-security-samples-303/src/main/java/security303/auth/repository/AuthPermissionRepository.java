package security303.auth.repository;

import java.util.List;

import security303.auth.entity.AuthPermission;

/**
 * 权限仓库接口。
 */
public interface AuthPermissionRepository {

	/**
	 * 查询全部权限。
	 *
	 * @return 全部权限列表
	 */
	List<AuthPermission> findAll();

}
