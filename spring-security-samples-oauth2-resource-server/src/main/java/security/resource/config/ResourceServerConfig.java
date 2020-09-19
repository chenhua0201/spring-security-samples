package security.resource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

/**
 * 资源服务器配置。
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private RemoteTokenServices remoteTokenServices;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		final DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		final UserAuthenticationConverter userTokenConverter = new EnhancedUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);

		resources.tokenServices(remoteTokenServices);
	}

}
