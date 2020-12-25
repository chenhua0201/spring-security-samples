package security304.authz;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

/**
 * URL permission实现。
 */
@Component
public class UrlPermissionSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private AuthzCacheService authzCacheService;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * 查询URL关联了哪些角色。返回null或空集合表示该URL不需要授权。
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(final Object object) throws IllegalArgumentException {
		return authzCacheService.findByFilterInvocation((FilterInvocation) object);
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
