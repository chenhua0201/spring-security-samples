package security304.authz;

import lombok.Data;

/**
 * 用作缓存key。
 */
@Data
public class FilterInvocationCacheKey {

	private String requestUrl;

	private String httpMethod;

	public FilterInvocationCacheKey() {
	}

	public FilterInvocationCacheKey(String requestUrl, String httpMethod) {
		this.requestUrl = requestUrl;
		this.httpMethod = httpMethod;
	}
}
