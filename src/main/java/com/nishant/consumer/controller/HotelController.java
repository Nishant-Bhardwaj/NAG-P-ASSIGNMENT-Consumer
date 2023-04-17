package com.nishant.consumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nishant.consumer.dto.*;
import com.nishant.consumer.service.HotelService;
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

@RestController
@RequestMapping(path = "/consumer/hotel")
public class HotelController {

    Logger logger = LogManager.getLogger(HotelController.class);

    @Autowired
    HotelService hotelService;

    // Search All Hotels
    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllHotels(){
        logger.info("Request Received, Search All Hotels");
        return new ResponseEntity<>(hotelService.findAllHotels(), HttpStatus.OK);
    }

    // Search Hotels with parameters
    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/search")
    public ResponseEntity<String> getSearchedHotels(@RequestBody HotelSearchDTO hotel) throws JsonProcessingException {
        logger.info("Request Received, Search Hotel : "+ hotel);
        return new ResponseEntity<>(hotelService.searchHotel(hotel), HttpStatus.OK);
    }

    // Shortlist Hotel, takes hotelId and userId
    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/shortlist")
    public ResponseEntity<String> shortlistHotels(@RequestBody HotelShortlisted hotelShortlisted) throws Exception {
        logger.info("Request Received, Shortlist Hotels : "+ hotelShortlisted);
        return new ResponseEntity<>(hotelService.shortlistHotel(hotelShortlisted), HttpStatus.OK);
    }

    // Book Hotel
    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/book")
    public ResponseEntity<HotelBookings> bookHotels(@RequestBody HotelBookingDTO hotelBookingDTO) throws Exception {
        logger.info("Request Received, Book Hotels : "+ hotelBookingDTO);
        return new ResponseEntity<HotelBookings>(hotelService.bookHotel(hotelBookingDTO), HttpStatus.OK);
    }

    @RateLimiter(name = "RateLimitService", fallbackMethod = "getRateLimitFallBack")
    @PostMapping(path = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hotelBookingPayment(@RequestBody HotelBookings hotelBookings) {
        logger.info("Request for Hotel Booking Payment : "+ hotelBookings);

        return new ResponseEntity<String>(
                (String) hotelService.bookingPayment(hotelBookings),
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
