package tech.engix.client_service.service.exception.exceptions;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String msg) {
        super(msg);
    }
}
