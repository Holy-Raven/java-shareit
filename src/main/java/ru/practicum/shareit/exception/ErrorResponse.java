package ru.practicum.shareit.exception;

public class ErrorResponse {
    public String getError() {
        return error;
    }

    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}