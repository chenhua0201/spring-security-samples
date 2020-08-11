package security302.exception;

/**
 * 业务逻辑异常。
 * <p/>
 * 在异常上增加<code>errorCode</code>属性。
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 默认错误码
	 */
	private static final String DEFAULT_CODE = "";

	/**
	 * 默认错误信息
	 */
	private static final String DEFAULT_MESSAGE = "";

	/**
	 * 错误码
	 */
	private final String errorCode;

	/**
	 * 错误码为空字符串。错误信息为空字符串。异常为null。
	 */
	public ServiceException() {
		this(DEFAULT_CODE, DEFAULT_MESSAGE, null);
	}

	/**
	 * 使用ServiceError的错误码，错误信息为空字符串，异常为null。
	 *
	 * @param serviceError 业务错误
	 */
	public ServiceException(final ServiceError serviceError) {
		this(serviceError.getErrorCode(), DEFAULT_MESSAGE, null);
	}

	/**
	 * 使用ServiceError的错误码，自定义错误信息，异常为null。
	 *
	 * @param serviceError 业务错误
	 * @param message      错误信息
	 */
	public ServiceException(final ServiceError serviceError, final String message) {
		this(serviceError.getErrorCode(), message, null);
	}

	/**
	 * 使用ServiceError的错误码，自定义错误信息。
	 *
	 * @param serviceError 业务错误
	 * @param message      错误信息
	 * @param cause        错误原因
	 */
	public ServiceException(final ServiceError serviceError, final String message, final Throwable cause) {
		this(serviceError.getErrorCode(), message, cause);
	}

	/**
	 * 自定义错误信息。
	 *
	 * @param errorCode 错误码
	 * @param message   错误信息
	 * @param cause     错误原因
	 */
	public ServiceException(final String errorCode, final String message, final Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
