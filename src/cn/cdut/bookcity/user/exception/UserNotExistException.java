package cn.cdut.bookcity.user.exception;

/**
 * 用户不存在异常类
 * @author huangyong
 * @date 2018年4月21日 下午11:39:50
 */
public class UserNotExistException extends Exception{

	public UserNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
