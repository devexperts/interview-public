package com.devexperts.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorDto {

    private String message;

    private HttpStatus status;

    private LocalDateTime time;

    public ErrorDto(String message, HttpStatus status, LocalDateTime time) {
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
