package security303.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import security303.auth.token.TokenProperties;

/**
 * 认证token配置。
 */
@Configuration
@EnableConfigurationProperties({ TokenProperties.class })
public class AuthTokenConfig {

}
