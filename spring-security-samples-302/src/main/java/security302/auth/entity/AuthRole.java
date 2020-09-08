package security302.auth.entity;

import lombok.Data;

/**
 * 角色。
 */
@Data
public class AuthRole {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 标识
	 */
	private String identifier;

	/**
	 * 是否是超级用户
	 */
	private boolean superRole;

}
