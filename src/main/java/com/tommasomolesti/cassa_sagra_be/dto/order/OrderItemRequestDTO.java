package com.tommasomolesti.cassa_sagra_be.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemRequestDTO {
    private UUID articleId;
    private Integer quantity;
}