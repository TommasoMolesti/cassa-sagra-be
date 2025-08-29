package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.dto.article.ArticleRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.article.ArticleResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.article.UpdateArticleRequestDTO;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
@Tag(name = "Article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(
            @RequestBody ArticleRequestDTO articleRequest,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID creatorId = authenticatedUser.getId();

        ArticleResponseDTO newArticle = articleService.createArticle(articleRequest, creatorId);

        return ResponseEntity.ok(newArticle);
    }

    @GetMapping("/list/{partyId}")
    public ResponseEntity<List<ArticleResponseDTO>> getArticleList(
            @PathVariable UUID partyId,
            @AuthenticationPrincipal User currentUser
    ) {
        UUID userId = currentUser.getId();
        List<ArticleResponseDTO> articles = articleService.getPartyArticles(userId, partyId);
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get an Article by ID")
    public ResponseEntity<ArticleResponseDTO> getArticleById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID creatorId = authenticatedUser.getId();
        ArticleResponseDTO articleResponseDTO = articleService.getArticleById(id, creatorId);
        return ResponseEntity.ok().body(articleResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Article")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable UUID id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID creatorId = authenticatedUser.getId();

        articleService.deleteArticle(id, creatorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(
            @PathVariable UUID id,
            @RequestBody UpdateArticleRequestDTO updateRequest,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        ArticleResponseDTO updatedArticle = articleService.updateArticle(id, updateRequest, userId);
        return ResponseEntity.ok(updatedArticle);
    }
}
