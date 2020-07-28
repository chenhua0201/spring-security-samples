package security201.auth.entity;

import lombok.Data;

/**
 * 账号。
 */
@Data
public class AuthAccount {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 登录名
	 */
	private String username;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 是否启用
	 */
	private boolean enabled = true;

	/**
	 * 是否已删除
	 */
	private boolean deleted;

}