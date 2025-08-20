package com.tommasomolesti.cassa_sagra_be.mapper;

import com.tommasomolesti.cassa_sagra_be.dto.ArticleRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.ArticleResponseDTO;
import com.tommasomolesti.cassa_sagra_be.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(source = "party.id", target = "partyId")
    ArticleResponseDTO toDTO(Article article);

    Article toModel(ArticleRequestDTO articleRequestDTO);
}
