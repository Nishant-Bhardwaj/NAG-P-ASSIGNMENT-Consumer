package com.nishant.consumer.service.impl;

import com.nishant.consumer.service.SupplierService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    WebClient webClient;

    @Autowired
    DiscoveryClient discoveryClient;


    /**
     * Resilience4J implemented, configuration in application.properties:
     *  1. Circuit Breaker
     *  2. Retry Config.
     *
     * @author Nishant Bhardwaj
     */
    @Override
    @CircuitBreaker(name =  "CircuitBreakerService")
    @Retry(name = "RetryConfig")
    public Mono<String> getSupplierHealth() {

        URI supplierURI = discoveryClient.getInstances("SUPPLIER-SERVICE-NISHANT").get(0).getUri();

        return webClient.get()
                .uri(supplierURI + "/supplier/")
                .retrieve()
                .bodyToMono(String.class);

    }
}
