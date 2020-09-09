package security303.exception;

/**
 * 业务错误。
 *
 */
public interface ServiceError {

	/***
	 * 返回错误码。
	 *
	 * @return 错误码
	 */
	default String getErrorCode() {
		return getClass().getSimpleName() + "." + toString();
	}

}
