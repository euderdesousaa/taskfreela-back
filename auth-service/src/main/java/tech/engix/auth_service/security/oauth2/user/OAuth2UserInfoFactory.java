package tech.engix.auth_service.security.oauth2.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tech.engix.auth_service.model.enums.AuthProvider;
import tech.engix.auth_service.security.oauth2.OAuth2UserInfo;

import java.util.Map;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws Exception {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new Exception();
        }
    }

}