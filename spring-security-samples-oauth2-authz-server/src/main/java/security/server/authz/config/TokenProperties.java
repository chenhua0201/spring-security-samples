package security.server.authz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Token属性。
 */
@Data
@ConfigurationProperties(prefix = "auth.token")
public class TokenProperties {

	private String prefix = "";

}
