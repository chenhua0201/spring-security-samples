package security204.auth.token;

/**
 * 认证token服务接口。
 */
public interface TokenService {

	/**
	 * 创建。
	 *
	 * @param principal principal
	 * @param value     值
	 * @return token
	 */
	String create(Object principal, TokenValue value);

	void delete(String token);

	/**
	 * 查询值。
	 *
	 * @param token token
	 * @return 值
	 */
	TokenValue findByToken(String token);
}
