package com.tommasomolesti.cassa_sagra_be.dto.party;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PartyResumeDTO {
    private UUID id;
    private String name;
    private Long quantity;

    public PartyResumeDTO(UUID id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}