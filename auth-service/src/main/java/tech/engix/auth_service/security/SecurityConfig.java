package tech.engix.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import tech.engix.auth_service.security.handler.CustomJWTLogoutHandler;
import tech.engix.auth_service.security.jwt.AuthTokenFilter;
import tech.engix.auth_service.security.oauth2.*;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String OAUTH2_BASE_URI = "/oauth2/authorize";
    private static final String OAUTH2_REDIRECTION_ENDPOINT = "/oauth2/callback/*";

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final AuthTokenFilter tokenAuthenticationFilter;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomJWTLogoutHandler jwtLogoutHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, @Lazy HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, AuthTokenFilter tokenAuthenticationFilter, ClientRegistrationRepository clientRegistrationRepository, CustomJWTLogoutHandler jwtLogoutHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.jwtLogoutHandler = jwtLogoutHandler;
    }


    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/api/v1/recovery/**", "/api/v1/accounts/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/auth-service/v3/api-docs").permitAll()
                        .anyRequest()
                        .authenticated()
        );

        http
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .baseUri(OAUTH2_BASE_URI)
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                .authorizationRequestResolver(new CustomAuthorizationRequestResolver(clientRegistrationRepository, OAUTH2_BASE_URI))
                        )
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri(OAUTH2_REDIRECTION_ENDPOINT))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig.accessTokenResponseClient(authorizationCodeTokenResponseClient()))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                ) .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(jwtLogoutHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login")
                );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new CustomAccessTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRestOperations(restTemplate);

        return tokenResponseClient;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 8, 1 << 16, 4);
    }


}
