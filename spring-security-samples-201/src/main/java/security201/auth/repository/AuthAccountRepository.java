package security201.auth.repository;

import security201.auth.entity.AuthAccount;

/**
 * 账号仓库接口。
 */
public interface AuthAccountRepository {

	/**
	 * 根据登录名查询。
	 *
	 * @param username 登录名
	 * @return 账号，或null
	 */
	AuthAccount findByUsername(String username);

}
