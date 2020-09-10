package security304.auth.token;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 认证token属性。
 */
@Data
@ConfigurationProperties(prefix = "auth.token")
public class TokenProperties {

	/**
	 * 超时时长，默认30分钟
	 */
	private Duration timeout = Duration.ofMinutes(30);

	/**
	 * redis key前缀
	 */
	private String prefix = "auth:token";

	/**
	 * token头
	 */
	private String header = AUTHORIZATION;

	/**
	 * token值前缀
	 */
	private String headerValuePrefix = "Bearer ";

}
