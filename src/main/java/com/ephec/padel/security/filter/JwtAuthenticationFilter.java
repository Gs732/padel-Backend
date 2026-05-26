package com.ephec.padel.security.filter;

import com.ephec.padel.security.service.JwtService;
import com.ephec.padel.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si pas de token → on continue la chaîne de filtres
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (enlever "Bearer ")
        String token = authHeader.substring(7);

        // 4. Extraire le username du token
        String username = jwtService.extractUsername(token);

        // 5. Si username valide et pas encore authentifié
        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Charger l'utilisateur
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(username);

            // 7. Vérifier le token
            if (jwtService.isTokenValid(token, userDetails)) {

                // 8. Créer l'objet Authentication
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
                );

                // 9. Mettre dans le SecurityContext
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // 10. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}