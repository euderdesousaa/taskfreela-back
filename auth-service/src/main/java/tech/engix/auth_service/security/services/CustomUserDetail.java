package tech.engix.auth_service.security.services;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import tech.engix.auth_service.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class CustomUserDetail implements UserDetails, OAuth2User {

    private String jwtToken;

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetail(Long id,
                         String email,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    public static CustomUserDetail create(User user) {
        List<GrantedAuthority> authorities = List.of();

        return new CustomUserDetail(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public static CustomUserDetail create(User user, Map<String, Object> attributes) {
        CustomUserDetail customUserDetail = CustomUserDetail.create(user);
        customUserDetail.setAttributes(attributes);
        return customUserDetail;
    }
}
