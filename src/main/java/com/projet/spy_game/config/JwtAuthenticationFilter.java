package com.projet.spy_game.config;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; 

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        // récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        // vérifier format Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extraire le token
        String token = authHeader.substring(7);
        String username = null;
        // extraire le username depuis le token
        try {
            username = jwtService.extractUsername(token);
}        catch (Exception e) {
            System.out.println("JWT error: " + e.getMessage());
        }

        // si user valide et pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // créer une authentification Spring
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            new ArrayList<>()
                    );

            // injecter dans le contexte
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // continuer la requête
        filterChain.doFilter(request, response);
    }
}