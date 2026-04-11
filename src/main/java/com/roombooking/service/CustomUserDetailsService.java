package com.roombooking.service;

import com.roombooking.model.Admin;
import com.roombooking.model.User;
import com.roombooking.repository.AdminRepository;
import com.roombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check admin first
        return adminRepository.findByEmail(email)
            .<UserDetails>map(admin -> new org.springframework.security.core.userdetails.User(
                admin.getEmail(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ))
            .orElseGet(() -> userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email))
            );
    }
}
