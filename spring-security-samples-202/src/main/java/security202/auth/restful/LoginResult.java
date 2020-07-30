package security202.auth.restful;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应。
 */
@Data
@Builder
public class LoginResult {

	private String userId;

	private String username;

}
