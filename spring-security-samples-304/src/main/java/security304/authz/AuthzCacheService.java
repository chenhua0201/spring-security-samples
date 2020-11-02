package security304.authz;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;

/**
 * 授权缓存服务接口。
 */
public interface AuthzCacheService {

	/**
	 * 清空。
	 */
	default void clear() {
		// 无操作
	}

	/**
	 * 查询{@link FilterInvocation}关联的{@link ConfigAttribute}。
	 *
	 * @param filterInvocation FilterInvocation
	 * @return FilterInvocation关联的ConfigAttribute
	 */
	Collection<ConfigAttribute> findByFilterInvocation(FilterInvocation filterInvocation);

	/**
	 * 查询超级角色标识列表。
	 *
	 * @return 超级角色标识列表
	 */
	Set<String> findSuperRoleIdentifiers();

}
