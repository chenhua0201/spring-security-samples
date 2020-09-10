package security304.auth.repository;

import java.util.List;

import security304.auth.entity.AuthPermission;

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
