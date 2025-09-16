package com.dzk.common.exception;


/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    
	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
} 