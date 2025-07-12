package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.PermissionCreateDTO;
import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.entity.Permission;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-12T13:38:07+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toEntity(PermissionCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setCode( dto.getCode() );
        permission.setDescription( dto.getDescription() );

        return permission;
    }

    @Override
    public PermissionDTO toDTO(Permission entity) {
        if ( entity == null ) {
            return null;
        }

        PermissionDTO permissionDTO = new PermissionDTO();

        permissionDTO.setParentId( entityParentId( entity ) );
        permissionDTO.setActif( entity.isActif() );
        permissionDTO.setCode( entity.getCode() );
        permissionDTO.setDescription( entity.getDescription() );
        permissionDTO.setId( entity.getId() );

        return permissionDTO;
    }

    private UUID entityParentId(Permission permission) {
        if ( permission == null ) {
            return null;
        }
        Permission parent = permission.getParent();
        if ( parent == null ) {
            return null;
        }
        UUID id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
