package com.tommasomolesti.cassa_sagra_be.mapper;

import com.tommasomolesti.cassa_sagra_be.dto.PartyResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.UserRequestDTO;
import com.tommasomolesti.cassa_sagra_be.model.Party;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartyMapper {
    PartyResponseDTO toDTO(Party party);
    Party toModel(UserRequestDTO userRequestDTO);
}
