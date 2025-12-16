package com.lanfunoe.gocache.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * HTTP状态码
     */
    private final HttpStatus httpStatus;

    public BusinessException(String message) {
        this(500, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(Integer code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(Integer code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        this(httpStatus.value(), message, httpStatus);
    }

    /**
     * 参数错误异常
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 未授权异常
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 禁止访问异常
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(403, message, HttpStatus.FORBIDDEN);
    }

    /**
     * 资源未找到异常
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message, HttpStatus.NOT_FOUND);
    }

    /**
     * 服务器错误异常
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException internalServerError(String message) {
        return new BusinessException(500, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}