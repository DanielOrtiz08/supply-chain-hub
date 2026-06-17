package com.example.productservice;

import com.example.productservice.config.CustomLoadBalancerConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "inventory-service",
        configuration = CustomLoadBalancerConfig.class) // Por defecto Round Robin, lo cambiamos por personalizado
public interface InventoryClient {

    @GetMapping("/api/inventory/{product}")
    boolean isInStock(@PathVariable("product") String product);
}
