package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.dto.UtilisateurCreateDTO;
import com.assureplus.auth.dto.UtilisateurDTO;
import com.assureplus.auth.entity.LanguePreferee;
import com.assureplus.auth.entity.Permission;
import com.assureplus.auth.entity.Role;
import com.assureplus.auth.entity.Utilisateur;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-12T14:15:10+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UtilisateurMapperImpl implements UtilisateurMapper {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Utilisateur toEntity(UtilisateurCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setRole( utilisateurCreateDTOToRole( dto ) );
        utilisateur.setEmail( dto.getEmail() );
        utilisateur.setIdentifiant( dto.getIdentifiant() );
        if ( dto.getLanguePreferee() != null ) {
            utilisateur.setLanguePreferee( Enum.valueOf( LanguePreferee.class, dto.getLanguePreferee() ) );
        }
        utilisateur.setPassword( dto.getPassword() );
        utilisateur.setTelephone( dto.getTelephone() );

        return utilisateur;
    }

    @Override
    public UtilisateurDTO toDTO(Utilisateur entity) {
        if ( entity == null ) {
            return null;
        }

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();

        utilisateurDTO.setRoleId( entityRoleId( entity ) );
        utilisateurDTO.setActif( entity.isActif() );
        utilisateurDTO.setEmail( entity.getEmail() );
        utilisateurDTO.setId( entity.getId() );
        utilisateurDTO.setIdentifiant( entity.getIdentifiant() );
        if ( entity.getLanguePreferee() != null ) {
            utilisateurDTO.setLanguePreferee( entity.getLanguePreferee().name() );
        }
        utilisateurDTO.setPermissions( permissionSetToPermissionDTOSet( entity.getPermissions() ) );
        utilisateurDTO.setTelephone( entity.getTelephone() );

        return utilisateurDTO;
    }

    protected Role utilisateurCreateDTOToRole(UtilisateurCreateDTO utilisateurCreateDTO) {
        if ( utilisateurCreateDTO == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( utilisateurCreateDTO.getRoleId() );

        return role;
    }

    private UUID entityRoleId(Utilisateur utilisateur) {
        if ( utilisateur == null ) {
            return null;
        }
        Role role = utilisateur.getRole();
        if ( role == null ) {
            return null;
        }
        UUID id = role.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<PermissionDTO> permissionSetToPermissionDTOSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionDTO> set1 = new LinkedHashSet<PermissionDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionMapper.toDTO( permission ) );
        }

        return set1;
    }
}
