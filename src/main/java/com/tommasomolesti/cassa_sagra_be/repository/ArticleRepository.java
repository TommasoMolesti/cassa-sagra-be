package com.tommasomolesti.cassa_sagra_be.repository;

import com.tommasomolesti.cassa_sagra_be.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("SELECT MAX(a.sortIndex) FROM Article a WHERE a.party.id = :partyId")
    Optional<Integer> findMaxSortIndexByPartyId(Integer partyId);
}
