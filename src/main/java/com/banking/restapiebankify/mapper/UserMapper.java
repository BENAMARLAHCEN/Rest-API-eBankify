package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "role.name", target = "role")
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}
