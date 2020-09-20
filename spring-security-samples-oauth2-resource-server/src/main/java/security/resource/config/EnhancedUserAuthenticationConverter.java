package security.resource.config;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 按照spring-security-samples-oauth2-authz-server的TokenEnhancer的格式，读取额外的token信息。
 */
@Service
class EnhancedUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

	private Collection<? extends GrantedAuthority> defaultAuthorities;

	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
			final Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

			final String userId = (String) map.get("userId");
			final String username = (String) map.get("username");

			final AuthAccountUserDetails user = new AuthAccountUserDetails(userId, username, null);
			return new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		final Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
