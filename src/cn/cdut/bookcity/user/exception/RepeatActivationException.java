package cn.cdut.bookcity.user.exception;

/**
 * 重复激活用户异常
 * @author huangyong
 * @date 2018年4月21日 下午11:45:45
 */
public class RepeatActivationException extends Exception {

	public RepeatActivationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RepeatActivationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RepeatActivationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RepeatActivationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RepeatActivationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
