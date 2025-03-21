package com.programmingtechie.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.order_service.dto.OrderLineItemsDto;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.model.OrderLineItems;
import com.programmingtechie.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import com.programmingtechie.order_service.model.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        // Buyurtmani saqlashdan oldin Inventory service'ga Kafka orqali xabar jo‘natamiz
        String orderJson = convertOrderToJson(order);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("inventory-check", orderJson);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                throw new RuntimeException("Kafka xabar jo‘natishda xatolik yuz berdi", ex);
            }
        });

        // Buyurtmani saqlash
        orderRepository.save(order);
    }

    private String convertOrderToJson(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Buyurtmani JSON formatga o‘tkazishda xatolik", e);
        }
    }
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemDto.getPrice());
        orderLineItems.setQuantity(orderLineItemDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemDto.getSkuCode());

        return orderLineItems;
    }
}





















//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final WebClient webClient;
//
//    public void placeOrder(OrderRequest orderRequest) {
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//
//        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//        order.setOrderLineItemsList(orderLineItemsList);
//
//        List<String> skuCodes = orderLineItemsList.stream()
//                .map(OrderLineItems::getSkuCode)
//                .toList();
//
//        // `InventoryService` orqali mahsulot zaxirasini tekshirish
//        InventoryResponse[] inventoryResponses = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/api/inventory")
//                        .queryParam("skuCode", skuCodes)
//                        .build())
//                .retrieve()
//                .bodyToMono(InventoryResponse[].class)
//                .block();
//
//        boolean allProductsInStock = Arrays.stream(inventoryResponses)
//                .allMatch(InventoryResponse::isInStock);
//
//        if (allProductsInStock) {
//            orderRepository.save(order);
//        } else {
//            throw new IllegalArgumentException("Mahsulot zaxirada yo‘q, keyinroq urinib ko‘ring.");
//        }
//    }
//
//    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto) {
//        OrderLineItems orderLineItems = new OrderLineItems();
//        orderLineItems.setPrice(orderLineItemDto.getPrice());
//        orderLineItems.setQuantity(orderLineItemDto.getQuantity());
//        orderLineItems.setSkuCode(orderLineItemDto.getSkuCode());
//
//        return orderLineItems;
//    }
//}
