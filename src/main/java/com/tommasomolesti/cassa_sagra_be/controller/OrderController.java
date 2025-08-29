package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.dto.order.OrderRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.order.OrderResponseDTO;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders/{partyId}")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable UUID partyId,
            @RequestBody OrderRequestDTO orderRequest,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        OrderResponseDTO createdOrder = orderService.createOrder(partyId, orderRequest, userId);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get an Order by ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        OrderResponseDTO order = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/list")
    @Operation(summary = "Get all orders from party")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersList(
            @PathVariable UUID partyId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        List<OrderResponseDTO> orders = orderService.getOrdersList(partyId, userId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        orderService.deleteOrder(orderId, userId);

        return ResponseEntity.noContent().build();
    }
}