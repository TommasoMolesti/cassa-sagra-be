package com.tommasomolesti.cassa_sagra_be.repository;

import com.tommasomolesti.cassa_sagra_be.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT MAX(o.orderCounter) FROM Order o WHERE o.party.id = :partyId")
    Optional<Integer> findMaxOrderCounterByPartyId(UUID partyId);
    List<Order> findAllByPartyId(UUID partyId);
}