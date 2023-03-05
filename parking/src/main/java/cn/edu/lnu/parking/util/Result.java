package cn.edu.lnu.parking.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;//编码 200 成功 非200 失败
    private String msg;
    private T data;

    public Result(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Result() {
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    public static Result success() {
        return success(null);
    }

    public static <T> Result<T> error(Integer code ,String msg) {
        return new Result<>(code,msg);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(10000,msg);
    }
    public static <T> Result<T> unauthorized() {
        return new Result<>(401,"登录状态失效，请重新登录");
    }

    @Override
    public String toString() {
        return "Status{"
                + "code='" + code
                + '\'' + ", msg='"
                + msg + '\''
                + ", data=" + data
                + '}';
    }

}
