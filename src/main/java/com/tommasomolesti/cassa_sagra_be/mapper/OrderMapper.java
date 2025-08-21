package com.tommasomolesti.cassa_sagra_be.mapper;

import com.tommasomolesti.cassa_sagra_be.dto.ArticleOrderedResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.OrderResponseDTO;
import com.tommasomolesti.cassa_sagra_be.model.ArticleOrdered;
import com.tommasomolesti.cassa_sagra_be.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDTO toDTO(Order order);

    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "article.name", target = "articleName")
    @Mapping(source = "article.price", target = "price")
    ArticleOrderedResponseDTO toArticleOrderedDTO(ArticleOrdered articleOrdered);
}