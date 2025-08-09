package com.tommasomolesti.cassa_sagra_be.mapper;

import com.tommasomolesti.cassa_sagra_be.dto.UserRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.UserResponseDTO;
import com.tommasomolesti.cassa_sagra_be.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDTO(User user);
    User toModel(UserRequestDTO userRequestDTO);
}