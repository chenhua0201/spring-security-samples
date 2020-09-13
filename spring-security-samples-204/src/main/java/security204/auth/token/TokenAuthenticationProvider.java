package security204.auth.token;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 基于Token。
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {

	private final TokenService tokenService;

	public TokenAuthenticationProvider(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		final String token = ((TokenAuthenticationToken) authentication).getToken();
		final TokenValue tokenValue = tokenService.findByToken(token);
		if (tokenValue == null) {
			final BadCredentialsException e = new BadCredentialsException("Token不存在");
			// 减少stack trace
			e.setStackTrace(new StackTraceElement[0]);
			throw e;
		}

		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (TokenAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
