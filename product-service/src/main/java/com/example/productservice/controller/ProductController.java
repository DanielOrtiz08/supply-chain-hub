package com.example.productservice.controller;

import com.example.productservice.InventoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private InventoryClient inventoryClient;

    @GetMapping("/name/{name}")
    public String getProductStatus(@PathVariable String name) {
        boolean stock = inventoryClient.isInStock(name);

        if(stock) {
            return "El producto '" + name + "' está disponible y listo para envío";
        } else {
            return "Lo sentimos, el producto '" + name + "' está agotado";
        }
    }

    /* ******************************************************************************** */
    // Inyectar puerto para saber quien responde
    @Value("${server.port}")
    private String port;

    @GetMapping("/category/{name}")
    public boolean isElectronic(@PathVariable String name) {
        printInstance("category", "Product");
        return name.equalsIgnoreCase("iphone-17-pro");
    }

    private void printInstance(String path, String service) {
        try {
            String containerId = InetAddress.getLocalHost().getHostName();
            System.out.println("¡Petición de " + path + " recibida en el CONTENEDOR DOCKER ID: " + containerId + " con puerto " + port + "!");
        } catch (UnknownHostException e) {
            System.out.println("No se pudo obtener el ID en " + service);
        }
    }
}
