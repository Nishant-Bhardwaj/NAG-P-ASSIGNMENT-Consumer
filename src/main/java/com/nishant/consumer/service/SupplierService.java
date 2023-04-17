package com.nishant.consumer.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface SupplierService {

    Mono<String> getSupplierHealth();
}
