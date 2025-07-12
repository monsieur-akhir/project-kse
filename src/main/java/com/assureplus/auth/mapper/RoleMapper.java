package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.RoleCreateDTO;
import com.assureplus.auth.dto.RoleDTO;
import com.assureplus.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleCreateDTO dto);
    RoleDTO toDTO(Role entity);
} 