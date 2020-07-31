package security203.auth.restful;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应。
 */
@Data
@Builder
public class LoginResult {

	private String accountId;

	private String username;

}
