package com.tommasomolesti.cassa_sagra_be.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Integer id;
    private String name;
    private int orderCounter;
    private LocalDateTime orderedTime;
    private List<ArticleOrderedResponseDTO> orderedArticles;
}