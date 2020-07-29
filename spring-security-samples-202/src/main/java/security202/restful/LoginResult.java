package security202.restful;

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
