/**
 * 
 */
package com.dingdong.common.exception;

/**
 * 
 * 锁定失败例外 用于事务回滚
 * 
 * @author yushansoft
 * 
 */
public class LockFailureException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1684707630176379892L;

	public LockFailureException() {
		super();
	}

	public LockFailureException(String message) {
		super(message);
	}

	public LockFailureException(String message, Exception e) {
		super(message + "\n" + e.getLocalizedMessage());
	}
}
