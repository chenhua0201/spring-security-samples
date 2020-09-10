package security304.auth.restful;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import security304.auth.AuthAccountUserDetails;
import security304.auth.token.GrantedAuthorityConverter;
import security304.auth.token.TokenProperties;
import security304.auth.token.TokenService;
import security304.auth.token.TokenValue;

/**
 * 认证成功后，返回状态码200而不是301；同时返回用户信息。
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final GrantedAuthorityConverter grantedAuthorityConverter;

	private final ObjectMapper objectMapper;

	private final TokenProperties tokenProperties;

	private final TokenService tokenService;

	@Autowired
	public RestAuthenticationSuccessHandler(GrantedAuthorityConverter grantedAuthorityConverter,
			ObjectMapper objectMapper, TokenProperties tokenProperties, TokenService tokenService) {
		this.grantedAuthorityConverter = grantedAuthorityConverter;
		this.objectMapper = objectMapper;
		this.tokenProperties = tokenProperties;
		this.tokenService = tokenService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		// 返回用户信息
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		final AuthAccountUserDetails userDetails = (AuthAccountUserDetails) token.getPrincipal();

		// 生成并存储token
		final String authToken = tokenService.create(userDetails, TokenValue.builder()
				.accountId(userDetails.getId())
				.username(userDetails.getUsername())
				.authorities(grantedAuthorityConverter.encode(userDetails.getAuthorities()))
				.build());

		final LoginResult result = LoginResult.builder()
				.accountId(userDetails.getId())
				.username(userDetails.getUsername())
				.build();

		// header里返回token
		response.addHeader(tokenProperties.getHeader(), authToken);

		objectMapper.writeValue(response.getOutputStream(), result);
	}

}
