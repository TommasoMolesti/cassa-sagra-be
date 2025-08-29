package com.tommasomolesti.cassa_sagra_be.dto.order;

import com.tommasomolesti.cassa_sagra_be.dto.article.ArticleOrderedResponseDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private UUID id;
    private String name;
    private int orderCounter;
    private LocalDateTime orderedTime;
    private List<ArticleOrderedResponseDTO> orderedArticles;
    private Float total;
}