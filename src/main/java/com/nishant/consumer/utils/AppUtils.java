package com.nishant.consumer.utils;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class AppUtils {

    @Autowired
    DiscoveryClient discoveryClient;

    /**
     * Return Flight Service URI using Eureka
     */
    public String getFlightServiceURI() {
        try {
            return discoveryClient.getInstances("FLIGHT-SUPPLIER-SERVICE-NISHANT")
                    .get(0).getUri().toString();
        }catch (Exception e){
            return "http://localhost:8084";
        }
    }


    /**
     * Return Hotel Service URI using Eureka
     */
    public String getHotelServiceURI() {
        try {
            return discoveryClient.getInstances("HOTEL-SUPPLIER-SERVICE-NISHANT")
                    .get(0).getUri().toString();
        }catch (Exception e){
            return "http://localhost:8085";
        }
    }

}
