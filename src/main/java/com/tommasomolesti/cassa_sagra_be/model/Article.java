package com.tommasomolesti.cassa_sagra_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(length = 50, nullable = false)
    private String name;

    private Integer quantity;

    @Column(name = "tracking_quantity", nullable = false)
    private boolean trackingQuantity;

    @Column(nullable = false)
    private float price;

    @Column(name = "sort_index")
    private Integer sortIndex;
}