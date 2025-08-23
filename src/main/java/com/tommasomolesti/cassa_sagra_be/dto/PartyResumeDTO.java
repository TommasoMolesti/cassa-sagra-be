package com.tommasomolesti.cassa_sagra_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyResumeDTO {
    private Integer id;
    private String name;
    private Long quantity;

    public PartyResumeDTO(Integer id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}