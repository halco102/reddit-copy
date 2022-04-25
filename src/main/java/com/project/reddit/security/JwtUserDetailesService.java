package com.project.reddit.security;

import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtUserDetailesService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var getUserByUsername = userRepository.getUserByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (!getUserByUsername.isEmpty()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(getUserByUsername.get().getUserRole().name()));

            return CustomUserDetailsImp.build(getUserByUsername.get());
            //return new CustomUserDetailsImp(getUserByUsername.get().getUsername(), getUserByUsername.get().getEmail(), getUserByUsername.get().getPassword(), grantedAuthorities);
        }else {
            throw new UsernameNotFoundException("User with username " + username + " was not found!");
        }

    }
}
