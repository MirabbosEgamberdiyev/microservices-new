package com.programmingtechie.inventory_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.inventory_service.dto.InventoryResponse;
import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.model.Order;
import com.programmingtechie.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()
                    ).toList();
    }


    @KafkaListener(topics = "inventory-check", groupId = "inventory-service-group")
    public void checkInventory(ConsumerRecord<String, String> record) {
        try {
            Order order = objectMapper.readValue(record.value(), Order.class);
            boolean allInStock = order.getOrderLineItemsList().stream()
                    .allMatch(item -> inventoryRepository.existsBySkuCode(item.getSkuCode()));

            if (allInStock) {
                System.out.println("Barcha mahsulotlar mavjud!");
            } else {
                System.out.println("Ba'zi mahsulotlar omborda yo‘q!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
