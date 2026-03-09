package com.cruise.booking.service;

import com.cruise.booking.entity.User;
import com.cruise.booking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No account found with email: " + email));

        String role = (user.getRole() != null && !user.getRole().isBlank()) ? user.getRole() : "ROLE_USER";
        // Spring's .roles() helper prepends "ROLE_" — strip it if already present
        String roleName = role.startsWith("ROLE_") ? role.substring(5) : role;

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles(roleName)
                .build();
    }
}
