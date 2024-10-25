package tech.engix.project_service.service.exception.exceptions;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String message) {
        super(message);
    }
}
