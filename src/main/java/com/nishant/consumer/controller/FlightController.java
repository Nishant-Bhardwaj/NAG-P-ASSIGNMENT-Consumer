package com.nishant.consumer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nishant.consumer.dto.Flight;
import com.nishant.consumer.dto.FlightBookingRequest;
import com.nishant.consumer.dto.FlightBookings;
import com.nishant.consumer.service.FlightService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/consumer/flight")
public class FlightController {

    Logger logger = LogManager.getLogger(FlightController.class);

    @Autowired
    FlightService flightService;

    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchFlights(@RequestBody Flight flight) {
        logger.info("Search Flight for flight filters: " + flight);
        return new ResponseEntity<>(flightService.searchFlights(flight), HttpStatus.OK);
    }

    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> allFlights(){
        logger.info("Search All Flights");
        return new ResponseEntity<>(flightService.findAllFlights(), HttpStatus.OK);
    }

    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> flightBooking(@RequestBody FlightBookingRequest flightBookingRequest) {
        logger.info("Request for Flight Booking : "+ flightBookingRequest);

        return new ResponseEntity<String>(
                (String) flightService.bookFlight(flightBookingRequest),
                HttpStatus.OK
        );
    }

    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> flightBookingPayment(@RequestBody FlightBookings flightBookings) {
        logger.info("Request for Flight Booking Payment : "+ flightBookings);

        return new ResponseEntity<String>(
                (String) flightService.bookingPayment(flightBookings),
                HttpStatus.OK
        );
    }

    public Mono<String> getRateLimitMonoFallBack(RequestNotPermitted exception) {
        logger.info("Rate limit has applied, So no further calls are getting accepted");
        return Mono.just("Too many requests : No further request will be accepted. Please try after sometime");
    }

    public ResponseEntity<String> getRateLimitFallBack(RequestNotPermitted exception) {
        logger.info("Rate limit has applied, So no further calls are getting accepted");
        return new ResponseEntity<String>("Too many requests : No further request will be accepted. Please try after sometime", HttpStatus.OK);
    }

}
