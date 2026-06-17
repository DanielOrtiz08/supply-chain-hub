package com.example.inventoryservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    // Inyectar puerto para saber quien responde
    @Value("${server.port}")
    private String port;

    @GetMapping("/{product}")
    public boolean isInStock(@PathVariable String product) {
        // Imprimir la instancia
        try {
            String containerId = InetAddress.getLocalHost().getHostName();
            System.out.println("¡Petición recibida en el CONTENEDOR DOCKER ID: " + containerId + " con puerto " + port + "!");
        } catch (UnknownHostException e) {
            System.out.println("No se pudo obtener el ID");
        }
        return !"iphone-17-pro".equalsIgnoreCase(product);
    }
}
