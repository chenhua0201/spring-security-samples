package security304.auth.entity;

import lombok.Data;

/**
 * 角色与权限的关联。
 */
@Data
public class AuthRolePermission {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 角色ID，关联auth_role表
	 */
	private String roleId;

	/**
	 * 权限ID，关联auth_permission表
	 */
	private String permissionId;

}
