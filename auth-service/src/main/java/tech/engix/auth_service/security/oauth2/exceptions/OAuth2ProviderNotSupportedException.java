package tech.engix.auth_service.security.oauth2.exceptions;

public class OAuth2ProviderNotSupportedException extends RuntimeException {
    public OAuth2ProviderNotSupportedException(String message) {
        super(message);
    }
}
