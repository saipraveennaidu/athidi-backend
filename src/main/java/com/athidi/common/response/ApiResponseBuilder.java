package com.athidi.common.response;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApiResponseBuilder {
    public <T> ApiResponse<T> success(
            String message,
            T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
