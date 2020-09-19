package security.server.authz.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import security.server.authz.auth.AuthAccountUserDetails;

/**
 * Token配置。Token增强，加入账号ID等。
 */
@Configuration
@EnableConfigurationProperties(TokenProperties.class)
class TokenConfig {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	private TokenProperties tokenProperties;

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(4);
			final AuthAccountUserDetails user = (AuthAccountUserDetails) authentication.getUserAuthentication()
					.getPrincipal();
			additionalInfo.put("userId", user.getId());
			additionalInfo.put("username", user.getUsername());

			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}

	@Bean
	public TokenStore tokenStore() {
		final RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix(tokenProperties.getPrefix());
		return tokenStore;
	}
}
