package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.UtilisateurCreateDTO;
import com.assureplus.auth.dto.UtilisateurDTO;
import com.assureplus.auth.entity.Utilisateur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface UtilisateurMapper {
    UtilisateurMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(UtilisateurMapper.class);

    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "permissions", ignore = true)
    Utilisateur toEntity(UtilisateurCreateDTO dto);

    @Mapping(target = "roleId", source = "role.id")
    UtilisateurDTO toDTO(Utilisateur entity);
} 