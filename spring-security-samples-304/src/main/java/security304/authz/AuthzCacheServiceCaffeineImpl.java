package security304.authz;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;
import security304.auth.entity.AuthPermission;
import security304.auth.entity.AuthRole;
import security304.auth.repository.AuthRoleRepository;
import security304.authz.event.AuthzRefreshEvent;

/**
 * 授权缓存服务Caffeine实现。
 */
@Service
@Slf4j
public class AuthzCacheServiceCaffeineImpl implements AuthzCacheService {

	/**
	 * 超级角色缓存key
	 */
	private static final String AUTHZ_SUPER_ROLE_IDENTIFIER = "AUTHZ_SUPER_ROLE_IDENTIFIER";

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Autowired
	private ConfigAttributeService configAttributeService;

	/**
	 * FilterInvocation关联的ConfigAttribute缓存
	 */
	private final Cache<FilterInvocationCacheKey, Collection<ConfigAttribute>> filterInvocationCache = Caffeine
			.newBuilder()
			.build();

	/**
	 * 超级角色缓存
	 */
	private final Cache<String, Set<String>> superRoleIdentifierCache = Caffeine.newBuilder()
			.build();

	@Override
	public void clear() {
		log.info("clear authz cache");

		superRoleIdentifierCache.invalidateAll();

		filterInvocationCache.invalidateAll();
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * 规则：
	 * <ul>
	 * <li>{@link AuthPermission}未定义的URL，不受保护；</li>
	 * <li>超级角色可以访问所有URL。</li>
	 * </ul>
	 */
	@Override
	public Collection<ConfigAttribute> findByFilterInvocation(FilterInvocation filterInvocation) {
		return filterInvocationCache.get(
				new FilterInvocationCacheKey(filterInvocation.getRequestUrl(), filterInvocation.getHttpRequest()
						.getMethod()),
				filterInvocationCacheKey -> configAttributeService.findByFilterInvocation(filterInvocation));
	}

	@Override
	public Set<String> findSuperRoleIdentifiers() {
		return superRoleIdentifierCache.get(AUTHZ_SUPER_ROLE_IDENTIFIER, key -> {
			final List<AuthRole> superRoles = authRoleRepository.findSuper();
			if (superRoles != null) {
				return superRoles.parallelStream()
						.map(AuthRole::getIdentifier)
						.collect(Collectors.toSet());
			} else {
				return Collections.emptySet();
			}
		});
	}

	/**
	 * 处理授权缓存刷新。
	 *
	 * @param event 授权缓存刷新事件
	 */
	@EventListener
	public void refresh(AuthzRefreshEvent event) {
		clear();
	}

}
