package com.nishant.consumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nishant.consumer.constant.Constants;
import com.nishant.consumer.dto.*;
import com.nishant.consumer.entity.User;
import com.nishant.consumer.repository.UserRepository;
import com.nishant.consumer.service.HotelService;
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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    Logger logger = LogManager.getLogger(HotelServiceImpl.class);

    @Autowired
    AppUtils appUtils;

    @Autowired
    UserRepository userRepository;

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "fallbackAllHotels"
    )
    public List<Hotel> findAllHotels() {
        logger.info("HotelService: findAllHotels --> START");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<HotelSearchDTO> httpEntity = new HttpEntity<>(null, httpHeaders);

        List flightList = new RestTemplate()
                .exchange(appUtils.getHotelServiceURI() + "/supplier/hotel/all",
                        HttpMethod.GET, httpEntity, List.class).getBody();

        return (ArrayList<Hotel>) flightList;
    }

    public List<Hotel> fallbackAllHotels(Throwable t) {
        // Log the error
        logger.error("Error occurred while finding all hotels: {}", t.getMessage());

        // Return a fallback response
        List<Hotel> fallbackHotels = new ArrayList<>();
        fallbackHotels.add(new Hotel("No hotels available at the moment"));
        return fallbackHotels;
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "fallbackSearchHotels"
    )
    public String searchHotel(HotelSearchDTO hotel) {
        logger.info("HotelService: SearchHotels --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<HotelSearchDTO> httpEntity = new HttpEntity<>(hotel, httpHeaders);

        String response = new RestTemplate()
                .exchange(appUtils.getHotelServiceURI() + "/supplier/hotel/search",
                        HttpMethod.POST, httpEntity, String.class).getBody();

        return response;
    }

    public String fallbackSearchHotels(HotelSearchDTO hotel, Throwable t) {
        // Log the error
        logger.error("Error occurred while searching for hotels: {}", t.getMessage());

        // Return a fallback response
        return "Sorry, we are unable to search for hotels at the moment. Please try again later.";
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "fallbackShortlistHotels"
    )
    public String shortlistHotel(HotelShortlisted hotelShortlisted) throws Exception {
        logger.info("HotelService: shortlistHotel --> START");

        // Validate User by userId:
        Optional<User> user = userRepository.findById(hotelShortlisted.getUserId());
        if (!user.isPresent()) {
            throw new Exception("InValid User!!");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<HotelShortlisted> httpEntity = new HttpEntity<>(hotelShortlisted, httpHeaders);
        String response = null;

        try {
            response = new RestTemplate()
                    .exchange(appUtils.getHotelServiceURI() + "/supplier/hotel/shortlist",
                            HttpMethod.POST, httpEntity, String.class).getBody();

        } catch (Exception e) {
            if (e.getMessage().contains("Hotel not found"))
                throw new Exception("Hotel Not Found!!");
            throw new Exception("Hotel Service Provider issue: " + e.getMessage());
        }

        return response.toString() + ", for user: " + user.get().getUsername();
    }

    public String fallbackShortlistHotels(HotelShortlisted hotelShortlisted, Throwable t) {
        // Log the error
        logger.error("Error occurred while shortlisting the hotel: {}", t.getMessage());

        // Return a fallback response
        return "Sorry, we are unable to shortlist the hotel at the moment. Please try again later.";
    }

    @Override
    public String getShortlistedHotels(Long userId) {
        logger.info("HotelService: Get shortlistHotel --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<Long> httpEntity = new HttpEntity<>(userId, httpHeaders);
        String response = null;

        try {
            response = new RestTemplate()
                    .exchange(
                            appUtils.getHotelServiceURI() + "/supplier/hotel/shortlist/get?userId=" + userId,
                            HttpMethod.POST, httpEntity, String.class
                    ).getBody();

        } catch (Exception e) {
            logger.error("Hotel Service Provider issue: " + e.getMessage());
        }

        return response;
    }

    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "fallbackBookHotels"
    )
    public HotelBookings bookHotel(HotelBookingDTO hotelBookingDTO) {
        logger.info("HotelService: Book Hotel --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<HotelBookingDTO> httpEntity = new HttpEntity<>(hotelBookingDTO, httpHeaders);
        HotelBookings response = null;

        try {
            response = new RestTemplate()
                    .exchange(
                            appUtils.getHotelServiceURI() + "/supplier/hotel/book",
                            HttpMethod.POST, httpEntity, HotelBookings.class
                    ).getBody();

        } catch (Exception e) {
            logger.error("Hotel Service Provider issue: " + e.getMessage());
        }

        return response;
    }

    public HotelBookings fallbackBookHotels(HotelBookingDTO hotelBookingDTO, Throwable t) {
        // Log the error
        logger.error("Error occurred while booking the hotel: {}", t.getMessage());

        // Return a fallback response
        HotelBookings fallbackBooking = new HotelBookings();
        fallbackBooking.setStatus("Failed");
        fallbackBooking.setRoomNumber("Sorry, we are unable to book the hotel at the moment. Please try again later.");
        return fallbackBooking;
    }


    @Override
    @CircuitBreaker(
            name = "CircuitBreakerService",
            fallbackMethod = "fallbackPaymentHotels"
    )
    public Object bookingPayment(HotelBookings hotelBookings) {
        logger.info("HotelService: Book Hotel Payment --> START");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.AUTH_CONSUMER_KEY);
        HttpEntity<HotelBookings> httpEntity = new HttpEntity<>(hotelBookings, httpHeaders);

        return new RestTemplate()
                .exchange(
                        appUtils.getHotelServiceURI() + "/supplier/hotel/payment",
                        HttpMethod.POST, httpEntity, String.class
                ).getBody();

    }

    // Fallback handler:
    public Object fallbackPaymentHotels(HotelBookings hotelBookings, Throwable t) {
        // Log the error
        logger.error("Error occurred while processing the payment for hotel booking: {}", t.getMessage());

        // Return a fallback response
        return "Sorry, we are unable to process the payment for the hotel booking at the moment. Please try again later.";
    }
}
