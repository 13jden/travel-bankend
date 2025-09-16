package com.dzk.common.exception;

import java.io.Serial;

/**
 * 验证异常
 */
public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5241799976840897795L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
