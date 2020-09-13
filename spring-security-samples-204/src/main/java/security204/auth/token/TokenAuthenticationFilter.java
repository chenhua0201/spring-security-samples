package security204.auth.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import security204.auth.AuthAccountUserDetails;

/**
 * 认证token过滤器。
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;

	private final GrantedAuthorityConverter grantedAuthorityConverter;

	private final TokenProperties tokenProperties;

	private final TokenService tokenService;

	public TokenAuthenticationFilter(AuthenticationManager authenticationManager,
			GrantedAuthorityConverter grantedAuthorityConverter, TokenProperties tokenProperties,
			TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.grantedAuthorityConverter = grantedAuthorityConverter;
		this.tokenProperties = tokenProperties;
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (SecurityContextHolder.getContext()
				.getAuthentication() == null) {
			final String bearer = request.getHeader(tokenProperties.getHeader());
			if (StringUtils.isNotBlank(bearer)) {
				final String token = StringUtils.removeStart(bearer, tokenProperties.getHeaderValuePrefix())
						.trim();

				// 尝试认证
				authenticationManager.authenticate(new TokenAuthenticationToken(token));

				final TokenValue tokenValue = tokenService.findByToken(token);

				// 设置UsernamePasswordAuthenticationToken
				final AuthAccountUserDetails userDetails = new AuthAccountUserDetails(tokenValue.getAccountId(),
						tokenValue.getUsername(), null/* password */);

				// principal是UserDetails
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null/* password */, grantedAuthorityConverter.decode(tokenValue.getAuthorities()));

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Store to SecurityContextHolder
				SecurityContextHolder.getContext()
						.setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

}
