package com.assureplus.auth.mapper;

import com.assureplus.auth.dto.PermissionCreateDTO;
import com.assureplus.auth.dto.PermissionDTO;
import com.assureplus.auth.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "parent", ignore = true)
    Permission toEntity(PermissionCreateDTO dto);

    @Mapping(target = "parentId", source = "parent.id")
    PermissionDTO toDTO(Permission entity);
} 