package com.example.inventoryservice.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    // Inyectar puerto para saber quien responde
    @Value("${server.port}")
    private String port;

    @GetMapping("/stock/{product}")
    public boolean isInStock(@PathVariable String product) {
        printInstance("stock", "Inventory");
        return !"iphone-17-pro".equalsIgnoreCase(product);
    }

    private void printInstance(String path, String service) {
        try {
            String containerId = InetAddress.getLocalHost().getHostName();
            System.out.println("¡Petición de " + path + " recibida en el CONTENEDOR DOCKER ID: " + containerId + " con puerto " + port + "!");
        } catch (UnknownHostException e) {
            System.out.println("No se pudo obtener el ID en " + service);
        }
    }

    /* ******************************************************************************** */
    private final RestClient restClient;
    public InventoryController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @GetMapping("/storage-category/{product}")
    public String storageByCategory(@PathVariable String product) {
        Boolean isElectronicResponse = restClient.get()
                .uri("http://product-service/api/products/category/{product}", product)
                .retrieve().body(Boolean.class);

        if(!isElectronicResponse) {
            return "No es dispositivo electronico, almacenando en estanteria convencional para mercancia general";
        } else {
            return "Dispositivo electronico, almacenando en bodega seca con control de humedad";
        }
    }
}
