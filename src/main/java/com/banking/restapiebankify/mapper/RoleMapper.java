package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.RoleDTO;
import com.banking.restapiebankify.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toRoleDTO(Role role);

    Role toRole(RoleDTO roleDTO);
}
