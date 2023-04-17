package com.nishant.consumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nishant.consumer.constant.Constants;
import com.nishant.consumer.dto.Flight;
import com.nishant.consumer.dto.FlightBookingRequest;
import com.nishant.consumer.dto.FlightBookings;
import com.nishant.consumer.service.FlightService;
import com.nishant.consumer.utils.AppUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    Logger logger = LogManager.getLogger(FlightServiceImpl.class);

    @Autowired
    AppUtils appUtils;

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "defaultSearchFlights"
    )
    public String searchFlights(Flight flight) {
        logger.info("FlightService: SearchFlights --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<Flight> httpEntity = new HttpEntity<>(flight, httpHeaders);

        String response = new RestTemplate()
                .exchange(appUtils.getFlightServiceURI() + "/supplier/flight/search",
                        HttpMethod.POST, httpEntity, String.class).getBody();

        return response;

    }

    //Fallback Method if circuit Breaks
    public String defaultSearchFlights(Flight flight, Throwable t) {
        // Log the error
        logger.error("Error occurred while searching for flights: {}", t.getMessage());

        // Return a fallback response
        return "Sorry, we are unable to search for flights at the moment. Please try again later.";
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "defaultAllFlights"
    )
    public List<Flight> findAllFlights() {
        logger.info("FlightService: findAllFlights --> START");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<Flight> httpEntity = new HttpEntity<>(null, httpHeaders);

        List flightList = new RestTemplate()
                .exchange(appUtils.getFlightServiceURI() + "/supplier/flight/all",
                        HttpMethod.GET, httpEntity, List.class).getBody();

        return (ArrayList<Flight>) flightList;
    }

    //Fallback Method if circuit Breaks
    public List<Flight> defaultAllFlights(Throwable t){
        logger.info("FLIGHT SERVICE: FETCH ALL FLIGHTS DOWN.. , Return blank from Fallback");
        List<Flight> blankList = new ArrayList<Flight>();
        blankList.add(new Flight("Circuit Breaker: No Flight Found at this time."));
        return blankList;
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "defaultBookFlights"
    )
    public Object bookFlight(FlightBookingRequest flightBookingRequest) {
        logger.info("FlightService: Book Flight --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<FlightBookingRequest> httpEntity = new HttpEntity<>(flightBookingRequest, httpHeaders);

        return new RestTemplate()
                .exchange(appUtils.getFlightServiceURI() + "/supplier/flight/book",
                        HttpMethod.POST, httpEntity, String.class).getBody();

    }

    // Fallback of Circuit Breaker
    public Object defaultBookFlights(FlightBookingRequest flightBookingRequest, Throwable t) {
        if(t.getMessage().contains("\"status\":500")){
            return "Flight Not Found!";
        } else {
            return "Sorry, we are unable to book your flight at the moment. Please try again later... " + t.getMessage();
        }
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "defaultFlightPayment"
    )
    public Object bookingPayment(FlightBookings flightBookings) {
        logger.info("FlightService: Payment Flight --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<FlightBookings> httpEntity = new HttpEntity<>(flightBookings, httpHeaders);

        return new RestTemplate()
                .exchange(appUtils.getFlightServiceURI() + "/supplier/flight/payment",
                        HttpMethod.POST, httpEntity, String.class).getBody();
    }

    // Fallback of Circuit Breaker
    public Object defaultFlightPayment(FlightBookings flightBookings, Throwable t) {
        return "Circuit Breaker: Sorry, we are unable to process your payment at the moment. Please try again later.";
    }

}
