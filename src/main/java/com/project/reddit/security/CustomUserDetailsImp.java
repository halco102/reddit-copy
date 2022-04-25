package com.project.reddit.security;

import com.project.reddit.model.user.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@EqualsAndHashCode
public class CustomUserDetailsImp implements UserDetails {
    private Long id;

    private String username;

    private String email;

    private String password;

    private String imageUrl;


    private Set<GrantedAuthority> roles;

    public CustomUserDetailsImp() {
    }

    public CustomUserDetailsImp(String username, String email, String password, Set<GrantedAuthority> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public CustomUserDetailsImp(Long id, String username, String email, String password, String imageUrl, Set<GrantedAuthority> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.roles = roles;
    }

    public static CustomUserDetailsImp build(User user) {
        Set<GrantedAuthority> r = new HashSet<>();
        r.add(new SimpleGrantedAuthority(user.getUserRole().name()));
        return new CustomUserDetailsImp(
            user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getImageUrl(), r
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
