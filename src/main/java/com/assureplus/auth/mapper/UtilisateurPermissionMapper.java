package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.UtilisateurPermissionCreateDTO;
import com.assureplus.auth.dto.UtilisateurPermissionDTO;
import com.assureplus.auth.entity.UtilisateurPermission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UtilisateurPermissionMapper {
    UtilisateurPermissionMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(UtilisateurPermissionMapper.class);

    UtilisateurPermission toEntity(UtilisateurPermissionCreateDTO dto);
    UtilisateurPermissionDTO toDTO(UtilisateurPermission entity);
} 