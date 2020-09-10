package security.resource.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 账号实现的{@link UserDetails}。
 */
public class AuthAccountUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final boolean accountNonExpired;

	private final boolean accountNonLocked;

	// 角色名称
	private final Set<GrantedAuthority> authorities;

	private final boolean credentialsNonExpired;

	private final boolean enabled;

	private final String id;

	private final String password;

	private final String username;

	public AuthAccountUserDetails(String id, String username, String password) {
		this(id, username, password, true, true, true, true, Collections.emptySet());
	}

	public AuthAccountUserDetails(String id, String username, String password, boolean accountNonExpired,
			boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
			Set<GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AuthAccountUserDetails [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", accountNonExpired=");
		builder.append(accountNonExpired);
		builder.append(", accountNonLocked=");
		builder.append(accountNonLocked);
		builder.append(", credentialsNonExpired=");
		builder.append(credentialsNonExpired);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append("]");
		return builder.toString();
	}

}
