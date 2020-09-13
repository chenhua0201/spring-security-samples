package security204.auth.token;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 * 认证token的值。
 */
@Data
@Builder
public class TokenValue {

	private String accountId;

	private String username;

	/**
	 * 对应UserDetails的authorities
	 */
	private Set<String> authorities;

}
