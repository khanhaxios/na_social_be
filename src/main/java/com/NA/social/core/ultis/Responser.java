package com.NA.social.core.ultis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class Responser {
    private static int SUCCESS_CODE = 9999;

    private static int SERVER_ERROR_CODE = -1111;
    private static int BAD_REQUEST_CODE = 4444;

    public static ResponseEntity<ApiResponse> success(Object data, String message) {
        return ResponseEntity.ok(ApiResponse.builder().data(data).code(SUCCESS_CODE).messages(Collections.singletonList(message)).build());
    }

    public static ResponseEntity<ApiResponse> success(Object data) {
        return ResponseEntity.ok(ApiResponse.builder().data(data).code(SUCCESS_CODE).build());
    }

    public static ResponseEntity<ApiResponse> success() {
        return ResponseEntity.ok(ApiResponse.builder().code(SUCCESS_CODE).messages(List.of("Call Api Success!")).build());
    }

    public static ResponseEntity<ApiResponse> serverError(String message) {
        return ResponseEntity.internalServerError().body(ApiResponse.builder().code(SERVER_ERROR_CODE).messages(List.of(message)).build());
    }

    public static ResponseEntity<ApiResponse> badRequest(List<String> messages) {
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(BAD_REQUEST_CODE).messages(messages).build());
    }

    public static ResponseEntity<ApiResponse> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<ApiResponse> unAuth() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
