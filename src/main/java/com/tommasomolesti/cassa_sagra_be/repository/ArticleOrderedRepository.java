package com.tommasomolesti.cassa_sagra_be.repository;

import com.tommasomolesti.cassa_sagra_be.model.ArticleOrdered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleOrderedRepository extends JpaRepository<ArticleOrdered, Integer> {

    List<ArticleOrdered> findAllByArticleId(Integer articleId);
}