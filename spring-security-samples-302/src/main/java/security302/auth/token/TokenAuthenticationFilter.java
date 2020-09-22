package security302.auth.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import security302.auth.AuthAccountUserDetails;

/**
 * 认证token过滤器。
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final GrantedAuthorityConverter grantedAuthorityConverter;

	private final TokenProperties tokenProperties;

	private final TokenService tokenService;

	@Autowired
	public TokenAuthenticationFilter(GrantedAuthorityConverter grantedAuthorityConverter,
			TokenProperties tokenProperties, TokenService tokenService) {
		this.grantedAuthorityConverter = grantedAuthorityConverter;
		this.tokenProperties = tokenProperties;
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(tokenProperties.getHeader());

		if (StringUtils.isNotBlank(token)) {
			token = StringUtils.removeStartIgnoreCase(token, tokenProperties.getHeaderValuePrefix())
					.trim();
			final TokenValue tokenValue = tokenService.findByToken(token);
			if (tokenValue != null && SecurityContextHolder.getContext()
					.getAuthentication() == null) {
				// 设置UsernamePasswordAuthenticationToken
				final AuthAccountUserDetails userDetails = new AuthAccountUserDetails(tokenValue.getAccountId(),
						tokenValue.getUsername(), null/* password */);

				// principal是UserDetails
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null/* password */, grantedAuthorityConverter.decode(tokenValue.getAuthorities()));

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// 认证成功
				final SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authentication);
				SecurityContextHolder.setContext(context);
			}
		}

		filterChain.doFilter(request, response);
	}

}
