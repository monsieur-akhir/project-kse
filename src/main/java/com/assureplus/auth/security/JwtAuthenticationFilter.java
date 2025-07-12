package com.assureplus.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            if (tokenProvider.validateToken(token)) {
                username = tokenProvider.getUsernameFromJWT(token);
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extraction dynamique des rôles et permissions
            List<GrantedAuthority> authorities = new ArrayList<>();
            String role = tokenProvider.getRoleFromJWT(token);
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
            List<String> permissions = tokenProvider.getPermissionsFromJWT(token);
            if (permissions != null) {
                for (String perm : permissions) {
                    authorities.add(new SimpleGrantedAuthority(perm));
                }
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
} 