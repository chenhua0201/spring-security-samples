package security202.auth.restful;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 从JSON里提取username和password。
 */
@Slf4j
public class LoginFormAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String FORM_USERNAME_KEY = "username";

	private static final String FORM_PASSWORD_KEY = "password";

	private final ObjectMapper objectMapper;

	private String usernameParameter = FORM_USERNAME_KEY;

	private String passwordParameter = FORM_PASSWORD_KEY;

	private boolean postOnly = true;

	public LoginFormAuthenticationFilter(ObjectMapper objectMapper) {
		super(new AntPathRequestMatcher("/login", "POST"));
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod()
				.equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		final LoginForm loginForm = obtain(request);
		log.debug("Authentication username: {}", loginForm.getUsername());

		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				loginForm.getUsername(), loginForm.getPassword());
		setDetails(request, authRequest);

		try {
			return getAuthenticationManager().authenticate(authRequest);
		} catch (final AuthenticationException e) {
			log.debug("Authentication fail, username: {}", loginForm.getUsername());
			// 减少stack trace
			e.setStackTrace(new StackTraceElement[0]);
			throw e;
		}
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	private LoginForm obtain(HttpServletRequest request) {
		try (InputStream is = request.getInputStream()) {
			return objectMapper.readValue(is, LoginForm.class);
		} catch (final IOException e) {
			log.warn("Obtain login form error", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Provided so that subclasses may configure what is put into the authentication
	 * request's details property.
	 *
	 * @param request     that an authentication request is being created for
	 * @param authRequest the authentication request object that should have its
	 *                    details set
	 */
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the password from the
	 * login request..
	 *
	 * @param passwordParameter the parameter name. Defaults to "password".
	 */
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter. If
	 * set to true, and an authentication request is received which is not a POST
	 * request, an exception will be raised immediately and authentication will not
	 * be attempted. The <tt>unsuccessfulAuthentication()</tt> method will be called
	 * as if handling a failed authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from the
	 * login request.
	 *
	 * @param usernameParameter the parameter name. Defaults to "username".
	 */
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}
}
