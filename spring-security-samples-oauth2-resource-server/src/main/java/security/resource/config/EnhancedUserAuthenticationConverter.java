package security.resource.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;

/**
 * 按照spring-security-samples-oauth2-authz-server的TokenEnhancer的格式，读取额外的token信息。
 */
@Service
class EnhancedUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

	private final Collection<GrantedAuthority> defaultAuthorities = Collections.emptySet();

	private final String PASSWORD_NA = "N/A";

	@Override
	public Authentication extractAuthentication(final Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
			final Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

			final String userId = (String) map.get("userId");
			final String username = (String) map.get("username");

			final AuthAccountUserDetails user = new AuthAccountUserDetails(userId, username, PASSWORD_NA, authorities);
			return new UsernamePasswordAuthenticationToken(user, PASSWORD_NA, authorities);
		}
		return null;
	}

}
