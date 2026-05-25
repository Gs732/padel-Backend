package com.ephec.padel.security.controller;

import com.ephec.padel.security.model.AuthRequest;
import com.ephec.padel.security.model.AuthResponse;
import com.ephec.padel.security.service.JwtService;
import com.ephec.padel.security.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    public AuthController (AuthenticationManager authenticationManager,
                            UserDetailsServiceImpl userDetailsService,
                            JwtService jwtService) {
          this.authenticationManager = authenticationManager;
          this.userDetailsService = userDetailsService;
          this.jwtService = jwtService;
     }  

     @PostMapping("/login")
     public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // 1) Authentifier l' utilisateur
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword()));

        // 2) Charger les détails de l'utilisateur
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(request.getUsername());
        
        // 3) Générer le JWT
        String token = jwtService.generateToken(userDetails);

        // 4) Retourner le token
        return ResponseEntity.ok(new AuthResponse(token));

     }
    
}
