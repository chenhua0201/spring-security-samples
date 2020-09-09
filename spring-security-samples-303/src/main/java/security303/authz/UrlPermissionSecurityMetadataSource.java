package security303.authz;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import security303.auth.entity.AuthPermission;
import security303.auth.entity.AuthRole;
import security303.auth.repository.AuthPermissionRepository;
import security303.auth.repository.AuthRoleRepository;

/**
 * URL permission实现。
 * <p/>
 * 规则：
 * <ul>
 * <li>{@link AuthPermission}未定义的URL，不受保护；</li>
 * <li>超级角色可以访问所有URL。</li>
 * </ul>
 */
@Component
public class UrlPermissionSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Autowired
	private AuthPermissionRepository authPermissionRepository;

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * 查询URL关联了哪些角色。
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// 请求的URL匹配的权限ID列表
		final Set<String> urlPermissionIds = authPermissionRepository.findAll()
				.parallelStream()
				.filter(permission -> isUrlAllowed(permission, (FilterInvocation) object))
				.map(AuthPermission::getId)
				.collect(Collectors.toSet());

		if (urlPermissionIds == null || urlPermissionIds.isEmpty()) {
			return Collections.emptySet();
		}

		// 权限对应的角色
		final List<AuthRole> roles = authRoleRepository.findByPermissionIds(urlPermissionIds);
		if (roles == null || roles.isEmpty()) {
			return Collections.emptySet();
		}

		// 角色标识集合列表
		final String[] attrs = roles.parallelStream()
				.map(AuthRole::getIdentifier)
				.collect(Collectors.toSet())
				.toArray(new String[0]);
		return SecurityConfig.createList(attrs);
	}

	private boolean isUrlAllowed(AuthPermission permission, FilterInvocation filterInvocation) {
		// URL不匹配
		if (!antPathMatcher.match(permission.getUrl(), filterInvocation.getRequestUrl())) {
			return false;
		}

		// 检查HTTP method
		final String permissionMethod = StringUtils.trimToEmpty(permission.getMethod());
		if ("*".equals(permissionMethod)) {
			// *表示任意HTTP method
			return true;
		}

		final String httpMethod = filterInvocation.getHttpRequest()
				.getMethod();
		final String[] methods = StringUtils.split(permissionMethod, ",");
		for (final String method : methods) {
			if (method.trim()
					.equalsIgnoreCase(httpMethod)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
