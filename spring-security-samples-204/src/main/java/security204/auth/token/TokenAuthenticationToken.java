package security204.auth.token;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 基于Token的{@link AuthenticationToken}。
 */
public class TokenAuthenticationToken implements Authentication {

	private static final long serialVersionUID = 1L;

	private final String token;

	private boolean authenticated = false;

	public TokenAuthenticationToken(String token) {
		this.token = token;

		setAuthenticated(true);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	/**
	 * Always returns an empty <code>String</code>
	 *
	 * @return an empty String
	 */
	@Override
	public Object getCredentials() {
		return "";
	}

	/**
	 * Always returns an empty <code>String</code>
	 *
	 * @return an empty String
	 */
	@Override
	public Object getDetails() {
		return "";
	}

	/**
	 * Always returns an empty <code>String</code>
	 *
	 * @return an empty String
	 */
	@Override
	public String getName() {
		return "";
	}

	/**
	 * Always returns an empty <code>String</code>
	 *
	 * @return an empty String
	 */
	@Override
	public Object getPrincipal() {
		return "";
	}

	public String getToken() {
		return token;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}
}
