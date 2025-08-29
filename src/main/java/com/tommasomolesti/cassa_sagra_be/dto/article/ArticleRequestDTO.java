package com.tommasomolesti.cassa_sagra_be.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ArticleRequestDTO {
    @NotBlank(message = "Article name is required")
    @Size(max = 50, message = "Article name cannot exceed 50 characters")
    private String name;

    private Integer quantity;
    private boolean trackingQuantity;
    private float price;
    private UUID partyId;
}
