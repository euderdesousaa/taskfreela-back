package tech.engix.tasks_service.service.exception.exceptions;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String msg) {
        super(msg);
    }
}
