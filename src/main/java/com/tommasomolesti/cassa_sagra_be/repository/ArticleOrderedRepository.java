package com.tommasomolesti.cassa_sagra_be.repository;

import com.tommasomolesti.cassa_sagra_be.dto.PartyResumeDTO;
import com.tommasomolesti.cassa_sagra_be.model.ArticleOrdered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleOrderedRepository extends JpaRepository<ArticleOrdered, Integer> {
    List<ArticleOrdered> findAllByArticleId(Integer articleId);
    @Query("SELECT new com.tommasomolesti.cassa_sagra_be.dto.PartyResumeDTO(ao.article.id, ao.article.name, SUM(ao.quantity)) " +
            "FROM ArticleOrdered ao " +
            "WHERE ao.order.party.id = :partyId " +
            "GROUP BY ao.article.id, ao.article.name " +
            "ORDER BY ao.article.name ASC")
    List<PartyResumeDTO> getPartyResume(Integer partyId);
}