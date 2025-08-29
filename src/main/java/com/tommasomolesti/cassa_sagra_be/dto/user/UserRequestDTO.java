package com.tommasomolesti.cassa_sagra_be.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;
}
