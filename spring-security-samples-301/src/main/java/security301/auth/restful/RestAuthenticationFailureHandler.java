package security301.auth.restful;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import security301.exception.ErrorConstants;
import security301.exception.LoginError;

/**
 * 认证失败后，返回状态码400、JSON错误消息。
 */
@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Map<String, String> LOGIN_FAIL = new HashMap<>(2);
	static {
		LOGIN_FAIL.put(ErrorConstants.CODE, LoginError.FAIL.getErrorCode());
		LOGIN_FAIL.put(ErrorConstants.MESSAGE, "账号或密码错误");
	}

	private final ObjectMapper objectMapper;

	@Autowired
	public RestAuthenticationFailureHandler(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		objectMapper.writeValue(response.getOutputStream(), LOGIN_FAIL);
	}

}
