package security303.authz;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import security303.auth.entity.AuthRole;
import security303.auth.repository.AuthRoleRepository;

/**
 * URL permission实现。
 */
@Component
public class UrlAccessDecisionVoter implements AccessDecisionVoter<Object> {

	@Autowired
	private AuthRoleRepository authRoleRepository;

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
		final Set<String> roleIdentifiers = authentication.getAuthorities()
				.parallelStream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		// 角色是否是超级角色
		final List<AuthRole> superRoles = authRoleRepository.findSuper();
		if (superRoles != null) {
			if (superRoles.parallelStream()
					.map(AuthRole::getIdentifier)
					.anyMatch(roleIdentifiers::contains)) {
				return ACCESS_GRANTED;
			}
		}

		// 角色是否包含attribute
		final boolean granted = attributes.parallelStream()
				.anyMatch(attr -> roleIdentifiers.contains(attr.getAttribute()));

		return granted ? ACCESS_GRANTED : ACCESS_DENIED;
	}

}
