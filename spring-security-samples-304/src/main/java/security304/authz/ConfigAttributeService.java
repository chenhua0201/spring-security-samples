package security304.authz;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;

/**
 * 确定{@link FilterInvocation}与{@link ConfigAttribute}的关联。
 */
public interface ConfigAttributeService {

	/**
	 * 查询{@link FilterInvocation}关联的{@link ConfigAttribute}。
	 *
	 * @param filterInvocation FilterInvocation
	 * @return FilterInvocation关联的ConfigAttribute
	 */
	Collection<ConfigAttribute> findByFilterInvocation(FilterInvocation filterInvocation);

}
