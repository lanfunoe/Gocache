package com.lanfunoe.gocache.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.Iterator;

/**
 * WebClient 日志过滤器
 *
 * 记录所有出站 HTTP 请求和响应
 * traceId/spanId 由 Spring Boot 的 logging.pattern.correlation 配置自动添加到日志中
 */
@Slf4j
@Component
public class WebClientLoggingFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Request attribute key for storing request body (用于在 filter 中获取请求体)
     */
    public static final String REQUEST_BODY_ATTR = "requestBody";

    public ExchangeFilterFunction loggingFilter() {
        return (request, next) -> {
            // 构建请求日志
            StringBuilder outboundRequest = new StringBuilder();
            outboundRequest.append("Method: ").append(request.method()).append("\n");
            outboundRequest.append("Url: ").append(request.url()).append("\n");
            outboundRequest.append("Headers: ");
            request.headers().forEach((name, values) ->
                    values.forEach(value -> outboundRequest.append(name).append("=").append(value).append(" ; "))
            );

            // 打印 POST 请求体 (从 request attribute 中获取)
            request.attribute(REQUEST_BODY_ATTR).ifPresent(body -> {
                try {
                    String bodyStr;
                    if (body instanceof String) {
                        bodyStr = (String) body;
                    } else {
                        bodyStr = OBJECT_MAPPER.writeValueAsString(body);
                    }
                    outboundRequest.append("\nBody: ").append(bodyStr);
                } catch (JsonProcessingException e) {
                    outboundRequest.append("\nBody: [serialization failed: ").append(e.getMessage()).append("]");
                }
            });

            log.info("\nOUTBOUND REQUEST:\n{}", outboundRequest);

            return next.exchange(request)
                    .flatMap(this::logResponse);
        };
    }

    /**
     * 打印响应详情
     */
    private Mono<ClientResponse> logResponse(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(responseStr -> {
                    JsonNode bodyNode;
                    try {
                        bodyNode = OBJECT_MAPPER.readTree(responseStr);
                    } catch (JsonProcessingException e) {
                        log.error(responseStr);
                        return response.mutate()
                                .body(responseStr)
                                .build();
                    }
                    String logBody = bodyNode.toPrettyString();
                    boolean isError = false;

                    // Check if body has data node with blank value
                    JsonNode dataNode = bodyNode.get("data");
                    if (dataNode != null && (dataNode.isNull() || (dataNode.isTextual() && StringUtils.isBlank(dataNode.asText())))) {
                        isError = true;
                    }

                    // Check if body has any field containing "error" in the key name with value length > 4
                    if (!isError && bodyNode.isObject()) {
                        Iterator<String> fieldNames = bodyNode.fieldNames();
                        while (fieldNames.hasNext()) {
                            String fieldName = fieldNames.next();
                            if (fieldName.toLowerCase().contains("error") || fieldName.toLowerCase().contains("error_code")) {
                                JsonNode fieldValue = bodyNode.get(fieldName);
                                if (fieldValue != null) {
                                    String value = fieldValue.asText();
                                    if (value.length() > 4) {
                                        isError = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    // Log response body
                    if (isError) {
                        log.error("OUTBOUND RESPONSE BODY:\n{}", logBody);
                    } else {
                        if (logBody.length() > 500) {
                            logBody = logBody.substring(0, 500) + "\n... [truncated]";
                        }
                        log.debug("\nOUTBOUND RESPONSE BODY:\n{}\n", logBody);
                    }
                    return response.mutate()
                            .body(responseStr)
                            .build();
                })
                .onErrorResume(error -> {
                    log.error("Failed to read response body: ", error);
                    return Mono.just(response);
                });
    }
}
