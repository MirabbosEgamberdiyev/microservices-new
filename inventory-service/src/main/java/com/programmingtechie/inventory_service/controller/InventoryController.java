package com.programmingtechie.inventory_service.controller;

import com.programmingtechie.inventory_service.dto.InventoryResponse;
import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam(name = "skuCode") List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }


}
