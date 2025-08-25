package com.authapp.exception;

import java.time.LocalDateTime;

public record ApiError(
    String message,
    int statusCode,
    LocalDateTime timestamp 
){}
