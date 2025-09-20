package com.hjhaju_web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name="users")
@Getter
@Setter
public class User implements UserDetails, OidcUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String fullName;

    @Column
    private String role;

    // Các thuộc tính cho OidcUser
    @Transient
    private Map<String, Object> attributes;
    @Transient
    private OidcIdToken idToken;
    @Transient
    private OidcUserInfo userInfo;

    // Các thuộc tính cho quên mật khẩu bằng OTP
    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpExpiry;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }

    public void setIdToken(OidcIdToken idToken) {
        this.idToken = idToken;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(OidcUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, Object> getClaims() {
        return idToken != null ? idToken.getClaims() : Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getName() {
        return this.fullName; // Trả về fullName từ DB
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Sử dụng email làm username
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
}