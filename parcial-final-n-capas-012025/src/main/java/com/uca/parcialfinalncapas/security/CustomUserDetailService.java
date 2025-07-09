package com.uca.parcialfinalncapas.security;

import com.uca.parcialfinalncapas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository; // Repository to access user data

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetches an user by username or email, throws an exception if not found
        User user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // Maps the user's roles to granted authorities
        Set<GrantedAuthority> grantedAuthorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Converts each role to a granted authority
                .collect(Collectors.toSet());

        // Returns a Spring Security User object with the user's username, password, and authorities
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}