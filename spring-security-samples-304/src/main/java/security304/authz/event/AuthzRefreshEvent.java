package security304.authz.event;

import org.springframework.context.ApplicationEvent;

/**
 * 授权缓存刷新事件。
 */
public class AuthzRefreshEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public AuthzRefreshEvent(Object source) {
		super(source);
	}

}
