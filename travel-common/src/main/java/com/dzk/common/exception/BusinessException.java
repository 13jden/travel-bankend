package com.dzk.common.exception;

import com.dzk.common.Enum.ResultEnum;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    
    private Integer code;
    
    public BusinessException(String message) {
        super(message);
        System.out.println(message);
        this.code = ResultEnum.AUTH_CODE_ERROR.code();
    }
    
    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public Integer getCode() {
        return code;
    }
} 