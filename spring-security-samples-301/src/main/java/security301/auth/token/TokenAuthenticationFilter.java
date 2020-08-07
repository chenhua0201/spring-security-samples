package security301.auth.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import security301.auth.AuthAccountUserDetailsServiceImpl;

/**
 * 认证token过滤器。
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final AuthAccountUserDetailsServiceImpl userDetailsService;

	private final TokenProperties tokenProperties;

	private final TokenService tokenService;

	@Autowired
	public TokenAuthenticationFilter(AuthAccountUserDetailsServiceImpl userDetailsService,
			TokenProperties tokenProperties, TokenService tokenService) {
		this.userDetailsService = userDetailsService;
		this.tokenProperties = tokenProperties;
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(tokenProperties.getHeader());

		if (StringUtils.isNotBlank(token)) {
			token = StringUtils.removeStart(token, tokenProperties.getHeaderValuePrefix())
					.trim();
			final TokenValue tokenValue = tokenService.findByToken(token);
			if (tokenValue != null) {
				final Authentication existingAuthentication = SecurityContextHolder.getContext()
						.getAuthentication();
				if (existingAuthentication == null) {
					// 没有用户信息，则重新加载
					final UserDetails userDetails = userDetailsService.loadUserByUsername(tokenValue.getUsername());

					final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, userDetails.getPassword(), userDetails.getAuthorities());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					// 认证成功
					SecurityContextHolder.getContext()
							.setAuthentication(authentication);
				}
			}
		}

		filterChain.doFilter(request, response);
	}

}
