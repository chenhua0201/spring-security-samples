package security304.authz;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 授权属性。
 */
@Data
@ConfigurationProperties(prefix = "authz")
public class AuthzProperties {

	/**
	 * 授权缓存redis topic
	 */
	private String cacheTopic = "authz:refresh";

}
