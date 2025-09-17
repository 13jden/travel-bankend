package com.dzk.common.exception;

import com.dzk.common.Enum.ResultEnum;
import com.dzk.common.common.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("请求参数错误: {}", errorMessage);
        return Result.error(ResultEnum.PARAM_IS_INVALID.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMessage = "请求参数错误";
        log.error("请求参数错误: {}", errorMessage);
        return Result.error(ResultEnum.PARAM_IS_INVALID.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Result handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("请求参数错误: {}", ex.getMessage());
        return Result.error(ResultEnum.PARAM_IS_INVALID.getMessage());
    }

    @ExceptionHandler(LoginTimeoutException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Result handleLoginTimeoutException(LoginTimeoutException ex) {
        log.error("登录超时: {}", ex.getMessage());
        return Result.error(ResultEnum.PERMISSION_EXPIRE.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleBusinessException(BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ResultEnum.BUSINESS_ERROR.code(),ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleValidationException(ValidationException ex) {
        log.error("校验异常: {}", ex.getMessage());
        return Result.error(ResultEnum.BUSINESS_ERROR.code(),ex.getMessage());
    }
}
