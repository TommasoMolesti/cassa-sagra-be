package com.tommasomolesti.cassa_sagra_be.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ArticleOrderedResponseDTO {
    private UUID articleId;
    private String articleName;
    private float price;
    private int quantity;
}