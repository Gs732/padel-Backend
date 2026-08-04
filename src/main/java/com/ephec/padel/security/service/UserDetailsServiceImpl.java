package com.ephec.padel.security.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Stockage en mémoire des utilisateurs
    // (plus tard on connectera à une vraie DB)
    private final Map<String, UserDetails> users = new HashMap<>();

    public UserDetailsServiceImpl() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Admin global : gère tout
        users.put("admin", User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN_GLOBAL")
                .build());

        // Admin de site : ne gère que son site (ici le site 1)
        users.put("admin_site", User.builder()
                .username("admin_site")
                .password(encoder.encode("site123"))
                .roles("ADMIN_SITE")
                .build());

        // Utilisateur normal
        users.put("user", User.builder()
                .username("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                "Utilisateur introuvable : " + username
            );
        }
        return user;
    }
}