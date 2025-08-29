package com.tommasomolesti.cassa_sagra_be.dto.party;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyResponseDTO {
    private String id;
    private String name;
    private String createdAt;
}
