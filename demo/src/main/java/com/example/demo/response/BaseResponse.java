package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public BaseResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }
}