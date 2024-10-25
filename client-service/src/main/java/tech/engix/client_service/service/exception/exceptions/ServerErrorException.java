package tech.engix.client_service.service.exception.exceptions;


public class ServerErrorException extends RuntimeException{
    public ServerErrorException(String message) {
        super(message);
    }
}
