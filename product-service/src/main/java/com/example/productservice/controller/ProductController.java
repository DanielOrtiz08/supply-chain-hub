package com.example.productservice.controller;

import com.example.productservice.InventoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private InventoryClient inventoryClient;

    @GetMapping("/{name}")
    public String getProductStatus(@PathVariable String name) {
        boolean stock = inventoryClient.isInStock(name);

        if(stock) {
            return "El producto '" + name + "' está disponible y listo para envío";
        } else {
            return "Lo sentimos, el producto '" + name + "' está agotado";
        }
    }
}
