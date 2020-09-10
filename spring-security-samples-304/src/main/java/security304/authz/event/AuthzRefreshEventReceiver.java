package security304.authz.event;

import org.springframework.context.ApplicationEventPublisher;

import lombok.extern.slf4j.Slf4j;

/**
 * 授权缓存接收redis订阅。
 */
@Slf4j
public class AuthzRefreshEventReceiver {

	private final ApplicationEventPublisher publisher;

	public AuthzRefreshEventReceiver(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	public void receive(String message) {
		log.info("receive authz refresh message");
		publisher.publishEvent(new AuthzRefreshEvent(message));
	}
}
