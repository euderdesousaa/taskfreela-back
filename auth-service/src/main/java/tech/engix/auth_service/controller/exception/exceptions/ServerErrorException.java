package tech.engix.auth_service.controller.exception.exceptions;


public class ServerErrorException extends RuntimeException{
    public ServerErrorException(String message) {
        super(message);
    }
}
