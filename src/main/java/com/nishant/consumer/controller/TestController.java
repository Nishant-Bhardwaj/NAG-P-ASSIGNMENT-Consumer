package com.nishant.consumer.controller;

import com.nishant.consumer.service.SupplierService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/consumer")
public class TestController {

    Logger logger = LogManager.getLogger(TestController.class);

    @Autowired
    SupplierService supplierService;

    @GetMapping(path = "/supplier/test")
    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    public Mono<String> checkSupplier(){
        return supplierService.getSupplierHealth();
    }

    public Mono<String> getRateLimitFallBack(RequestNotPermitted exception) {
        logger.info("Rate limit has applied, So no further calls are getting accepted");
        return Mono.just("Too many requests : No further request will be accepted. Please try after sometime");
    }
}
