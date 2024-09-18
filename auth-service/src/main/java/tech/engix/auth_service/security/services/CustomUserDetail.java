package tech.engix.auth_service.security.services;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import tech.engix.auth_service.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class CustomUserDetail implements UserDetails, OAuth2User {

    private final transient User user;
    private String jwtToken;

    private Long id;
    private String email;
    private String name;
    private String password;

    public CustomUserDetail(User user) {
        this.user = user;
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DEFAULT"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetail create(User user) {
        return new CustomUserDetail(user);
    }
}
