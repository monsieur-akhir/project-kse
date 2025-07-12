package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.RoleCreateDTO;
import com.assureplus.auth.dto.RoleDTO;
import com.assureplus.auth.entity.Role;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-12T13:38:07+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setDescription( dto.getDescription() );
        role.setNom( dto.getNom() );

        return role;
    }

    @Override
    public RoleDTO toDTO(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setActif( entity.isActif() );
        roleDTO.setDescription( entity.getDescription() );
        roleDTO.setId( entity.getId() );
        roleDTO.setNom( entity.getNom() );

        return roleDTO;
    }
}
