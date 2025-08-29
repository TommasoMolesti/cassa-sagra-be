package com.tommasomolesti.cassa_sagra_be.dto.order;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private String name;
    private List<OrderItemRequestDTO> items;
    private Float total;
}