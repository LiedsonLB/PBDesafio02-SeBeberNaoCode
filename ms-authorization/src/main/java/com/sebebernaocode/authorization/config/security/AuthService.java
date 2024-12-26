package com.sebebernaocode.authorization.config.security;

import com.sebebernaocode.authorization.entities.role.Role;
import com.sebebernaocode.authorization.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final UserService userService;
    private final TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.findByEmail(email);
    }

    public String getTokenAuthenticated(String email){
        Set<String> roles = userService.findByEmail(email)
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return tokenService.createToken(email, roles);
    }
}
