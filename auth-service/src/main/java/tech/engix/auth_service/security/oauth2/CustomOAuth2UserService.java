package tech.engix.auth_service.security.oauth2;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.model.enums.AuthProvider;
import tech.engix.auth_service.repositories.UserRepository;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.oauth2.exceptions.OAuth2EmailAlreadyRegisteredException;
import tech.engix.auth_service.security.oauth2.user.OAuth2UserInfoFactory;
import tech.engix.auth_service.security.services.CustomUserDetail;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtTokenUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("OAuth2User loaded: {}", oAuth2User.getAttributes());

            CustomUserDetail userDetail = (CustomUserDetail) processOAuth2User(userRequest, oAuth2User);

            Authentication authentication = getAuthentication(userDetail);
            String jwtToken = jwtTokenUtil.generateJwtToken(authentication);
            userDetail.setJwtToken(jwtToken);

            log.info("Generated JWT Token: {}", jwtToken);
            return userDetail;
        } catch (Exception ex) {
            log.error("Exception while loading user: {}", ex.getMessage(), ex);
            throw new InternalAuthenticationServiceException("An error occurred while loading the user", ex);
        }
    }


    public Authentication getAuthentication(CustomUserDetail userDetail) {
        return new UsernamePasswordAuthenticationToken(
                userDetail, null, userDetail.getAuthorities()
        );
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws Exception {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        AuthProvider provider = getOAuth2Provider(oAuth2UserRequest.getClientRegistration().getRegistrationId());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            log.error("Email not found from OAuth2 provider");
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        if (user != null) {
            if (!user.getAuthProvider().equals(provider)) {
                throw new OAuth2EmailAlreadyRegisteredException("This email has already been registered through "
                        + user.getAuthProvider() + ". Please use the same provider to log in.");
            }
            log.info("User found with email: " + oAuth2UserInfo.getEmail());
        } else {
            user = new User();
            user.setEmail(oAuth2UserInfo.getEmail());
            user.setName(oAuth2UserInfo.getName());
            user.setAuthProvider(provider);

            userRepository.save(user);

            log.info("New user registered with email: " + oAuth2UserInfo.getEmail());
        }

        return CustomUserDetail.create(user);
    }

    private AuthProvider getOAuth2Provider(String registrationId) {
        return switch (registrationId) {
            case "google" -> AuthProvider.GOOGLE;
            case "github" -> AuthProvider.GITHUB;
            default -> throw new IllegalArgumentException("Unknown OAuth2 provider: " + registrationId);
        };
    }
}
