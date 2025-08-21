package com.tommasomolesti.cassa_sagra_be.service;

import com.tommasomolesti.cassa_sagra_be.dto.ArticleRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.ArticleResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.UpdateArticleRequestDTO;
import com.tommasomolesti.cassa_sagra_be.exception.AccessDeniedException;
import com.tommasomolesti.cassa_sagra_be.exception.ArticleNotFoundException;
import com.tommasomolesti.cassa_sagra_be.exception.PartyNotFoundException;
import com.tommasomolesti.cassa_sagra_be.mapper.ArticleMapper;
import com.tommasomolesti.cassa_sagra_be.model.Article;
import com.tommasomolesti.cassa_sagra_be.model.Party;
import com.tommasomolesti.cassa_sagra_be.repository.ArticleRepository;
import com.tommasomolesti.cassa_sagra_be.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final PartyRepository partyRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(
            ArticleRepository articleRepository,
            PartyRepository partyRepository,
            ArticleMapper articleMapper
    ) {
        this.articleRepository = articleRepository;
        this.partyRepository = partyRepository;
        this.articleMapper = articleMapper;
    }

    public ArticleResponseDTO createArticle(ArticleRequestDTO articleRequest, UUID authenticatedUserId) {
        Integer partyId = articleRequest.getPartyId();
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));

        if (!party.getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to add articles to this party");
        }

        int newSortIndex = articleRepository.findMaxSortIndexByPartyId(partyId)
                .map(index -> index + 1)
                .orElse(0);

        Article newArticle = articleMapper.toModel(articleRequest);
        newArticle.setParty(party);
        newArticle.setSortIndex(newSortIndex);

        Article savedArticle = articleRepository.save(newArticle);

        return articleMapper.toDTO(savedArticle);
    }

    public List<ArticleResponseDTO> getPartyArticles(UUID userId, Integer partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));

        if(!party.getCreator().getId().equals(userId)) {
            throw new AccessDeniedException("User is not authorized to modify this party");
        }

        return party.getArticles().stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ArticleResponseDTO getArticleById(Integer id, UUID authenticatedUserId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + id));

        if (!article.getParty().getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to access this article");
        }

        return articleMapper.toDTO(article);
    }

    public void deleteArticle(Integer id, UUID authenticatedUserId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + id));

        if (!article.getParty().getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to access this article");
        }

        articleRepository.delete(article);
    }

    public ArticleResponseDTO updateArticle(
            Integer articleId,
            UpdateArticleRequestDTO updateRequest,
            UUID authenticatedUserId
    ) {
        Article articleToUpdate = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + articleId));

        if (!articleToUpdate.getParty().getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to modify this article");
        }

        articleToUpdate.setName(updateRequest.getName());
        articleToUpdate.setQuantity(updateRequest.getQuantity());
        articleToUpdate.setTrackingQuantity(updateRequest.isTrackingQuantity());
        articleToUpdate.setPrice(updateRequest.getPrice());
        articleToUpdate.setSortIndex(updateRequest.getSortIndex());

        Article updatedArticle = articleRepository.save(articleToUpdate);

        return articleMapper.toDTO(updatedArticle);
    }
}
