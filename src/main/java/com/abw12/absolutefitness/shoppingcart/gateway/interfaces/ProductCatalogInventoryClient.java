package com.abw12.absolutefitness.shoppingcart.gateway.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name ="product-inventory-controller")
public interface ProductCatalogInventoryClient {

    @GetMapping("/checkStockStatus")
    ResponseEntity<Map<String, Object>> cartValidation(@RequestParam Map<String,String> request);
}
