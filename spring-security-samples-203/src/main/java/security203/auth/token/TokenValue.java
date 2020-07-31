package security203.auth.token;

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
}
