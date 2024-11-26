package com.zelkova.zelkova.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zelkova.zelkova.dto.CommonResponse;

public class ApiResponseUtil {

    public static <T> ResponseEntity<CommonResponse<T>> success(T data) {
        return ResponseEntity.ok(
            new CommonResponse<>(HttpStatus.OK.value(), "Success", data)
        );
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(String message) {

        if (message.isEmpty()) {
            message = "Error";
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                new CommonResponse<>(
                    HttpStatus.BAD_REQUEST.value(), 
                    message, 
                    null
                )
            );
    }
}
