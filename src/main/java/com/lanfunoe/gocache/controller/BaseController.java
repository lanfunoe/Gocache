package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseController {
    protected Mono<ResponseEntity<Map<String, Object>>> handleOperation(
            String operationName,
            Mono<Map<String, Object>> operation,
            Object... logParams) {
        return operation
            .map(ResponseEntity::ok)
            .doOnError(e -> {
                if (logParams.length > 0) {
                    log.error("{} failed: {}", operationName, Arrays.toString(logParams), e);
                } else {
                    log.error("{} failed", operationName, e);
                }
            })
            .onErrorResume(this::handleException);
    }

    protected  <T> Mono<ResponseEntity<Map<String, Object>>> handleBoxOperation(
            String operationName,
            Mono<T> operation,
            Object... logParams) {
        return operation
                .map(res ->{
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", res);
                    response.put("status", 1);
                    response.put("error_code", 0);
                    return ResponseEntity.ok(response);
                })
                .doOnError(e -> {
                    if (logParams.length > 0) {
                        log.error("{} failed: {}", operationName, Arrays.toString(logParams), e);
                    } else {
                        log.error("{} failed", operationName, e);
                    }
                })
                .onErrorResume(this::handleException);
    }



    protected <T> Mono<ResponseEntity<T>> handleEntityOperation(
            String operationName,
            Mono<T> operation,
            Object... logParams) {
        return operation
            .map(ResponseEntity::ok)
            .doOnError(e -> {
                if (logParams.length > 0) {
                    log.error("{} failed: {}", operationName, Arrays.toString(logParams), e);
                } else {
                    log.error("{} failed", operationName, e);
                }
            })
            .onErrorResume(this::handleEntityException);
    }

    /**
     * Handles a reactive operation that already returns ResponseEntity with standardized error handling and logging.
     *
     * @param operationName The name of the operation for logging purposes
     * @param operation The reactive operation to execute (already returns ResponseEntity)
     * @param logParams Parameters to include in log messages
     * @return ResponseEntity wrapped in Mono with proper error handling
     */
    protected Mono<ResponseEntity<Map<String, Object>>> handleOperationWithResponse(
            String operationName,
            Mono<ResponseEntity<Map<String, Object>>> operation,
            Object... logParams) {

        // Log the operation start
        if (logParams.length > 0) {
            log.info("{} request: {}", operationName, Arrays.toString(logParams));
        } else {
            log.info("{} request", operationName);
        }

        return operation
            .doOnError(e -> {
                if (logParams.length > 0) {
                    log.error("{} failed: {}", operationName, Arrays.toString(logParams), e);
                } else {
                    log.error("{} failed", operationName, e);
                }
            })
            .onErrorResume(this::handleException);
    }

    /**
     * Centralized exception handling logic.
     *
     * @param e The exception to handle
     * @return Mono with appropriate error response
     */
    private Mono<ResponseEntity<Map<String, Object>>> handleException(Throwable e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("data", null);
        errorResponse.put("status", 0);

        if (e instanceof WebExchangeBindException bindEx) {
            Map<String, String> errors = new HashMap<>();
            bindEx.getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            errorResponse.put("error_code", 400);
            errorResponse.put("msg", "参数验证失败");
            errorResponse.put("errors", errors);
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        } else if (e instanceof BusinessException bizEx) {
            errorResponse.put("error_code", bizEx.getCode());
            errorResponse.put("msg", bizEx.getMessage());
            return Mono.just(ResponseEntity.status(bizEx.getHttpStatus()).body(errorResponse));
        } else {
            errorResponse.put("error_code", 500);
            errorResponse.put("msg", "服务器内部错误");
            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    /**
     * Centralized exception handling logic for entity operations.
     *
     * @param e The exception to handle
     * @param <T> The entity type
     * @return Mono with appropriate error response (cast to entity type for compatibility)
     */
    @SuppressWarnings("unchecked")
    private <T> Mono<ResponseEntity<T>> handleEntityException(Throwable e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("data", null);
        errorResponse.put("status", 0);

        if (e instanceof WebExchangeBindException bindEx) {
            Map<String, String> errors = new HashMap<>();
            bindEx.getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            errorResponse.put("error_code", 400);
            errorResponse.put("msg", "参数验证失败");
            errorResponse.put("errors", errors);
            return Mono.just(ResponseEntity.badRequest().body((T) errorResponse));
        } else if (e instanceof BusinessException bizEx) {
            errorResponse.put("error_code", bizEx.getCode());
            errorResponse.put("msg", bizEx.getMessage());
            return Mono.just(ResponseEntity.status(bizEx.getHttpStatus()).body((T) errorResponse));
        } else {
            errorResponse.put("error_code", 500);
            errorResponse.put("msg", "服务器内部错误");
            return Mono.just(ResponseEntity.internalServerError().body((T) errorResponse));
        }
    }
}