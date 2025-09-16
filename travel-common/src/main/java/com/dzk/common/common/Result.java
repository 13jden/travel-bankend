package com.dzk.common.common;
import com.dzk.common.Enum.ResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author mijiupro
 */

@Data
@Schema(description = "统一响应结果封装类")
@Slf4j
@NoArgsConstructor
public class Result<T> {

    @Schema(description = "操作代码", example = "200")
    Integer code;

    @Schema(description = "提示信息", example = "操作成功")
    String message;

    @Schema(description = "结果数据")
    T data;

    public Result(ResultEnum resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public Result(ResultEnum resultCode, T data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }
    public Result(String message) {
        this.message = message;
    }
    //成功返回封装-无数据
    public static Result<String> success() {
        return new Result<String>(ResultEnum.SUCCESS);
    }
    //成功返回封装-带数据
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultEnum.SUCCESS, data);
    }
    //失败返回封装-使用默认提示信息
    public static Result<String> error(Integer code, String message) {
        log.error("错误：msg:{},code:{}",message,code);
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    //失败返回封装-使用返回结果枚举提示信息
    public static Result<String> error(ResultEnum resultCode) {
        return new Result<String>(resultCode);
    }
    //失败返回封装-使用自定义提示信息
    public static Result<String> error(String message) {
        return new Result<String>(message);

    }
}