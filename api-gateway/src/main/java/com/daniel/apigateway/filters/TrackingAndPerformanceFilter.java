package com.daniel.apigateway.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TrackingAndPerformanceFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(TrackingAndPerformanceFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. Pre-Filter: Antes de redirigir al microservicio
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        String correlationID = UUID.randomUUID().toString(); // id unico para rastrear la peticion

        log.info("[GATEWAY PRE] Petición entrante: {} {} | Tracking ID: {}", method, path, correlationID);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate().header("X-Tracking-ID", correlationID).build())
                .build();

        // 2. Puente reactivo: (.then)
        // chain.filter(mutatedExchange) envía la petición al siguiente filtro o al microservicio.
        // El metodo .then() define una función lambda asíncrona que se ejecutará en el futuro,
        // JUSTO CUANDO EL MICROSERVICIO RESPONDA. Esto es la magia No-Bloqueante.
        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            // 3. Post-Filter: Al regresar del microservicio
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            int statusCode = exchange.getResponse().getStatusCode() != null ?
                    exchange.getResponse().getStatusCode().value() : 500;

            log.info("[GATEWAY POST] Respuesta recibida del microservicio para: {} | Status: {} | Tiempo de ejecución: {} ms | Tracking ID: {}",
                    path, statusCode, executionTime, correlationID);

            // Añadimos el ID de tracking a la respuesta final para que el cliente lo tenga en caso de reclamo/error
            exchange.getResponse().getHeaders().add("X-Response-Tracking-ID", correlationID);
        }));
    }

    @Override
    public int getOrder() {
        // Define la prioridad de este filtro en la cadena.
        // Ordered.HIGHEST_PRECEDENCE u orden 0 significa que será el PRIMER filtro en ejecutarse al entrar
        // y el ÚLTIMO en ejecutarse al salir.
        return 0;
    }
}

/*

[Cliente] ---> ( PREDICATE ) ---> [ PRE-FILTERS ] ---> [ Microservicio (Eureka/Docker) ]
                                                                    |
[Cliente] <---------------------- [ POST-FILTERS ] <----------------+

*/