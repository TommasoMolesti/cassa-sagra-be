package com.tommasomolesti.cassa_sagra_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleOrderedResponseDTO {
    private Integer articleId;
    private String articleName;
    private float price;
    private int quantity;
}