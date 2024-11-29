package tech.engix.auth_service.security.oauth2.exceptions;

public class OAuth2EmailAlreadyRegisteredException extends RuntimeException {
    public OAuth2EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
