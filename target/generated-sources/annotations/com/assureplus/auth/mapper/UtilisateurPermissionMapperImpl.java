package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.UtilisateurPermissionCreateDTO;
import com.assureplus.auth.dto.UtilisateurPermissionDTO;
import com.assureplus.auth.entity.UtilisateurPermission;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-12T13:38:07+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UtilisateurPermissionMapperImpl implements UtilisateurPermissionMapper {

    @Override
    public UtilisateurPermission toEntity(UtilisateurPermissionCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UtilisateurPermission utilisateurPermission = new UtilisateurPermission();

        return utilisateurPermission;
    }

    @Override
    public UtilisateurPermissionDTO toDTO(UtilisateurPermission entity) {
        if ( entity == null ) {
            return null;
        }

        UtilisateurPermissionDTO utilisateurPermissionDTO = new UtilisateurPermissionDTO();

        utilisateurPermissionDTO.setActif( entity.isActif() );

        return utilisateurPermissionDTO;
    }
}
