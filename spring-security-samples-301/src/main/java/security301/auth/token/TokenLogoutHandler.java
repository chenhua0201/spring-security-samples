package security301.auth.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * 认证token注销处理器。删除token。
 */
@Component
public class TokenLogoutHandler implements LogoutHandler {

	@Autowired
	private TokenProperties tokenProperties;

	@Autowired
	private TokenService tokenService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String token = request.getHeader(tokenProperties.getHeader());

		if (StringUtils.isNotBlank(token)) {
			tokenService.delete(StringUtils.removeStart(token, tokenProperties.getHeaderValuePrefix())
					.trim());
		}
	}

}
