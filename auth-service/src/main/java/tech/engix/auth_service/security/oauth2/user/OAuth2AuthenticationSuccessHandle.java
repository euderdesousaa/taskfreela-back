package tech.engix.auth_service.security.oauth2.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import tech.engix.auth_service.security.jwt.JwtUtils;

import java.io.IOException;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OAuth2AuthenticationSuccessHandle extends SimpleUrlAuthenticationSuccessHandler {

    @Value(value = "${tech.engix.url}")
    private String url;

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        String targetUrl = url + "/welcome?token=" + jwtToken;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
