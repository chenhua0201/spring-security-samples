package security303.auth.entity;

import lombok.Data;

/**
 * 权限。
 */
@Data
public class AuthPermission {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * ANT风格URL
	 */
	private String url;

	/**
	 * HTTP方法，逗号分隔，不区分大小写。*表示全部
	 */
	private String method;

}
