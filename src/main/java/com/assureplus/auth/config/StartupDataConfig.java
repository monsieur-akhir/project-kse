package com.assureplus.auth.config;

import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.entity.Role;
import com.assureplus.auth.entity.Utilisateur;
import com.assureplus.auth.entity.LanguePreferee;
import com.assureplus.auth.repository.PermissionRepository;
import com.assureplus.auth.repository.RoleRepository;
import com.assureplus.auth.repository.UtilisateurRepository;
import com.assureplus.auth.repository.UtilisateurPermissionRepository;
import com.assureplus.auth.entity.UtilisateurPermission;
import com.assureplus.auth.entity.UtilisateurPermissionId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
public class StartupDataConfig {
    @Bean
    public CommandLineRunner initRootUser(RoleRepository roleRepo, PermissionRepository permRepo, UtilisateurRepository userRepo, UtilisateurPermissionRepository utilisateurPermissionRepository) {
        return args -> {
            Logger log = LoggerFactory.getLogger(StartupDataConfig.class);
            // Création de toutes les permissions nécessaires au bon fonctionnement
            String[][] permissions = {
                {"PERM_ADMIN", "Permission administrateur"},
                {"PERM_USER", "Permission utilisateur"}
                // Ajoute ici d'autres permissions si besoin
            };
            for (String[] perm : permissions) {
                if (permRepo.findAll().stream().noneMatch(p -> p.getCode().equals(perm[0]))) {
                    Permission p = new Permission();
                    p.setId(UUID.randomUUID());
                    p.setCode(perm[0]);
                    p.setDescription(perm[1]);
                    p.setActif(true);
                    p.setCreatedAt(LocalDateTime.now());
                    p.setDeleted(false);
                    permRepo.save(p);
                }
            }

            // Création du rôle ROOT si absent
            Role rootRole = roleRepo.findAll().stream()
                .filter(r -> r.getNom().equalsIgnoreCase("ROOT"))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Création du rôle ROOT");
                    Role r = new Role();
                    r.setId(UUID.randomUUID());
                    r.setNom("ROOT");
                    r.setDescription("Super administrateur");
                    r.setActif(true);
                    r.setCreatedAt(LocalDateTime.now());
                    r.setDeleted(false);
                    return roleRepo.save(r);
                });

            // Création de l'utilisateur root si absent
            Utilisateur root = userRepo.findAll().stream()
                .filter(u -> u.getIdentifiant().equals("root"))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Création de l'utilisateur root");
                    Utilisateur u = new Utilisateur();
                    u.setId(UUID.randomUUID());
                    u.setIdentifiant("root");
                    u.setEmail("root@assureplus.com");
                    u.setLanguePreferee(LanguePreferee.FRANCAIS);
                    u.setRole(rootRole);
                    u.setActif(true);
                    u.setEstVerifie(true);
                    u.setCreatedAt(LocalDateTime.now());
                    u.setDeleted(false);
                    // Mot de passe par défaut hashé
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    u.setPassword(encoder.encode("kse123#"));
                    return userRepo.save(u);
                });

            // Donne toutes les permissions à root via la table d'association
            List<Permission> allPerms = permRepo.findAll();
            int count = 0;
            for (Permission permission : allPerms) {
                UtilisateurPermissionId upId = new UtilisateurPermissionId();
                upId.setUtilisateurId(root.getId());
                upId.setPermissionId(permission.getId());
                UtilisateurPermission up = new UtilisateurPermission();
                up.setId(upId);
                up.setUtilisateur(root);
                up.setPermission(permission);
                up.setActif(true);
                up.setCreatedAt(LocalDateTime.now());
                up.setDeleted(false);
                utilisateurPermissionRepository.save(up);
                count++;
            }
            log.info("Utilisateur root a {} permissions.", count);

            // Création de l'utilisateur franck si absent
            Utilisateur franck = userRepo.findAll().stream()
                .filter(u -> u.getIdentifiant().equals("franck"))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Création de l'utilisateur franck");
                    Utilisateur u = new Utilisateur();
                    u.setId(UUID.randomUUID());
                    u.setIdentifiant("franck");
                    u.setEmail("franck@email.com");
                    u.setLanguePreferee(LanguePreferee.FRANCAIS);
                    u.setRole(rootRole);
                    u.setActif(true);
                    u.setEstVerifie(true);
                    u.setCreatedAt(LocalDateTime.now());
                    u.setDeleted(false);
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    u.setPassword(encoder.encode("kse123#"));
                    return userRepo.save(u);
                });

            // Donne toutes les permissions à franck via la table d'association
            int countFranck = 0;
            for (Permission permission : allPerms) {
                UtilisateurPermissionId upId = new UtilisateurPermissionId();
                upId.setUtilisateurId(franck.getId());
                upId.setPermissionId(permission.getId());
                UtilisateurPermission up = new UtilisateurPermission();
                up.setId(upId);
                up.setUtilisateur(franck);
                up.setPermission(permission);
                up.setActif(true);
                up.setCreatedAt(LocalDateTime.now());
                up.setDeleted(false);
                utilisateurPermissionRepository.save(up);
                countFranck++;
            }
            log.info("Utilisateur franck a {} permissions.", countFranck);
        };
    }
} 