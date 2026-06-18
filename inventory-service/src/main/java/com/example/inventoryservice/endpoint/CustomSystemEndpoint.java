package com.example.inventoryservice.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// Creación de Endpoint personalizado de actuator
@Component
@Endpoint (id = "custom-system-info") // uri = actuator/customSystemInfo
public class CustomSystemEndpoint {

    @ReadOperation // Signigica que este metodo responde a una peticion HATTP GET
    public Map<String, String> getCustomDetails() {
        Map<String, String> details = new HashMap<>();
        details.put("developer", "Daniel");
        details.put("enviroment", "Docker-Compose Dev");
        details.put("activeInstancesCount", "3");
        return details;
    }
}
