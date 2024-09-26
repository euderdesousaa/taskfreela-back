package tech.engix.auth_service.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.oauth2")
@Slf4j
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();
    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }

    @Getter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }
}
