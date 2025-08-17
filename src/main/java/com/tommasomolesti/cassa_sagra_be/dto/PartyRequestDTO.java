package com.tommasomolesti.cassa_sagra_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRequestDTO {
    @NotBlank(message = "Party name is required")
    @Size(max = 50, message = "Party name cannot exceed 50 characters")
    private String name;
}
