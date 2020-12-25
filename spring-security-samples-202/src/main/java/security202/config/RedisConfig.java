package security202.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis配置。
 */
@Configuration
class RedisConfig {

	/**
	 * 值序列化改为JSON。
	 *
	 * @param redisConnectionFactory RedisConnectionFactory
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
		final RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setValueSerializer(RedisSerializer.json());
		return template;
	}

}
