package security304.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import security304.authz.AuthzProperties;
import security304.authz.event.AuthzRefreshEventReceiver;

/**
 * Redis订阅配置。
 */
@Configuration
class RedisListenerConfig {

	@Autowired
	private AuthzProperties authzProperties;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Bean
	public MessageListenerAdapter authRefreshMessageListenerAdapter() {
		return new MessageListenerAdapter(new AuthzRefreshEventReceiver(publisher), "receive");
	}

	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(authRefreshMessageListenerAdapter(),
				new ChannelTopic(authzProperties.getCacheTopic()));

		return container;
	}
}
