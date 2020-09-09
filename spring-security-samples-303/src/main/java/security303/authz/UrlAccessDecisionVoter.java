package security303.authz;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * URL permission实现。
 */
@Component
public class UrlAccessDecisionVoter implements AccessDecisionVoter<Object> {

	@Autowired
	private AuthzProperties authzProperties;

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		if (attributes == null || attributes.isEmpty()) {
			// 没有配置属性
			return authzProperties.isDefaultPermit() ? ACCESS_GRANTED : ACCESS_DENIED;
		}

		final Set<String> roleIdentifiers = authentication.getAuthorities()
				.parallelStream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		// 角色是否包含attribute
		final boolean granted = attributes.parallelStream()
				.anyMatch(attr -> roleIdentifiers.contains(attr.getAttribute()));

		return granted ? ACCESS_GRANTED : ACCESS_DENIED;
	}

}
