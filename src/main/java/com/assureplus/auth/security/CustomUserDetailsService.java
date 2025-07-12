package com.assureplus.auth.security;

import com.assureplus.auth.entity.Utilisateur;
import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.repository.UtilisateurRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findAll().stream()
                .filter(u -> u.getIdentifiant().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Ajout du rôle
        if (utilisateur.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().getNom().toUpperCase()));
        }
        // Ajout des permissions
        if (utilisateur.getPermissions() != null) {
            for (Permission perm : utilisateur.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(perm.getCode()));
            }
        }
        return User.withUsername(utilisateur.getIdentifiant())
                .password(utilisateur.getPassword())
                .authorities(authorities)
                .build();
    }
} 