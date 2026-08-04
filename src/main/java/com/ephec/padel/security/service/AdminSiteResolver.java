package com.ephec.padel.security.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AdminSiteResolver {

    // Association admin de site -> son site (en dur pour la démo).
    // En production, viendrait de la table utilisateur.
    private static final Map<String, Long> ADMIN_SITE_MAP = Map.of(
        "admin_site", 1L
    );

    /** Username de l'utilisateur actuellement authentifié. */
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

    /** True si l'utilisateur courant a le rôle ADMIN_GLOBAL. */
    public boolean isAdminGlobal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN_GLOBAL"));
    }

    /** Le site géré par l'admin de site courant, ou null s'il n'en gère pas. */
    public Long getSiteIdOfCurrentAdmin() {
        return ADMIN_SITE_MAP.get(getCurrentUsername());
    }
}