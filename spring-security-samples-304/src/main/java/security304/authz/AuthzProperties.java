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
	 * {@link AuthPermission}没有配置的URL，是允许还是拒绝
	 */
	private boolean defaultPermit = false;

	/**
	 * 授权缓存redis topic
	 */
	private String cacheTopic = "authz:refresh";
}
