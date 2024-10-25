package tech.engix.project_service.service.exception.exceptions;

public class ClientNotFound extends RuntimeException {
    public ClientNotFound(String msg) {
        super(msg);
    }
}
