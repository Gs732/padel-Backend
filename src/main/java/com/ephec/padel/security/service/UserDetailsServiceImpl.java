package com.ephec.padel.security.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ephec.padel.security.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Stockage en mémoire des utilisateurs
    // (plus tard on connectera à une vraie DB)
    private final Map<String, UserDetails> users = new HashMap<>();

    public UserDetailsServiceImpl() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Création de 2 utilisateurs par défaut
        users.put("admin", User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build());

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