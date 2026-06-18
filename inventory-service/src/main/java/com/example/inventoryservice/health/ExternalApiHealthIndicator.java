package com.example.inventoryservice.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

// Indicador de salud personalizado
public class ExternalApiHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isApiReachable = checkExternalApiStatus();

        if(isApiReachable) {
            return Health.up()
                    .withDetail("API Status", "Conexión exitosa")
                    .withDetail("Response Time", "45ms")
                    .build();
        } else {
            return Health.down()
                    .withDetail("API Status", "No responde")
                    .withException(new RuntimeException("Timeout al conectar con el proveedor externo"))
                    .build();
        }
    }

    private boolean checkExternalApiStatus() {
        return true; // simulación
    }
}
