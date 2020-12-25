package security304.authz;

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
	private AuthzCacheService authzCacheService;

	@Override
	public boolean supports(final Class<?> clazz) {
		return true;
	}

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		return true;
	}

	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes) {
		// 用户的角色标识列表
		final Set<String> accountRoleIdentifiers = authentication.getAuthorities()
				.parallelStream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		// 角色是否是超级角色
		if (authzCacheService.findSuperRoleIdentifiers()
				.parallelStream()
				.anyMatch(accountRoleIdentifiers::contains)) {
			return ACCESS_GRANTED;
		}

		// 角色是否包含attribute
		final boolean granted = attributes.parallelStream()
				.anyMatch(attr -> accountRoleIdentifiers.contains(attr.getAttribute()));

		return granted ? ACCESS_GRANTED : ACCESS_DENIED;
	}

}
