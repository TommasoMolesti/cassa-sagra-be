package com.tommasomolesti.cassa_sagra_be.service;

import com.tommasomolesti.cassa_sagra_be.dto.OrderRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.OrderResponseDTO;
import com.tommasomolesti.cassa_sagra_be.exception.ArticleNotFoundException;
import com.tommasomolesti.cassa_sagra_be.exception.InsufficientStockException;
import com.tommasomolesti.cassa_sagra_be.exception.OrderNotFoundException;
import com.tommasomolesti.cassa_sagra_be.exception.PartyNotFoundException;
import com.tommasomolesti.cassa_sagra_be.mapper.OrderMapper;
import com.tommasomolesti.cassa_sagra_be.model.*;
import com.tommasomolesti.cassa_sagra_be.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PartyRepository partyRepository;
    private final ArticleRepository articleRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, PartyRepository partyRepository, ArticleRepository articleRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.partyRepository = partyRepository;
        this.articleRepository = articleRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO createOrder(Integer partyId, OrderRequestDTO orderRequest, UUID authenticatedUserId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));
        if (!party.getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to create orders for this party");
        }

        int newOrderCounter = orderRepository.findMaxOrderCounterByPartyId(partyId).map(c -> c + 1).orElse(1);

        Order newOrder = new Order();
        newOrder.setName(orderRequest.getName());
        newOrder.setParty(party);
        newOrder.setOrderCounter(newOrderCounter);

        Set<ArticleOrdered> orderedArticles = new HashSet<>();
        for (var itemDto : orderRequest.getItems()) {
            Article article = articleRepository.findById(itemDto.getArticleId())
                    .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + itemDto.getArticleId()));

            if (article.isTrackingQuantity()) {
                int newQuantity = article.getQuantity() - itemDto.getQuantity();

                if (newQuantity < 0) {
                    throw new InsufficientStockException(
                            "Insufficient stock for article: " + article.getName() +
                                    ". Requested: " + itemDto.getQuantity() +
                                    ", Available: " + article.getQuantity()
                    );
                }

                article.setQuantity(newQuantity);
            }

            ArticleOrdered articleOrdered = new ArticleOrdered();
            articleOrdered.setOrder(newOrder);

            articleOrdered.setArticle(article);
            articleOrdered.setQuantity(itemDto.getQuantity());
            orderedArticles.add(articleOrdered);
        }
        newOrder.setOrderedArticles(orderedArticles);

        Order savedOrder = orderRepository.save(newOrder);

        return orderMapper.toDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Integer id, UUID userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with this id: " + id));
        if(!order.getParty().getCreator().getId().equals(userId)) {
            throw new AccessDeniedException("User is not authorized to access this article");
        }

        return orderMapper.toDTO(order);
    }

    public List<OrderResponseDTO> getOrdersList(Integer partyId, UUID userId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));

        if (!party.getCreator().getId().equals(userId)) {
            throw new AccessDeniedException("User is not authorized to view orders for this party");
        }

        return party.getOrders().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrder(Integer orderId, UUID authenticatedUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        if (!order.getParty().getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to delete this order");
        }

        for (ArticleOrdered item : order.getOrderedArticles()) {
            Article article = item.getArticle();

            if (article.isTrackingQuantity()) {
                int newQuantity = article.getQuantity() + item.getQuantity();
                article.setQuantity(newQuantity);
            }
        }

        orderRepository.delete(order);
    }
}