package com.nishant.consumer.service;

import com.nishant.consumer.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface HotelService {

    List<Hotel> findAllHotels();

    String searchHotel(HotelSearchDTO hotel);

    String shortlistHotel(HotelShortlisted hotelShortlisted) throws Exception;

    String getShortlistedHotels(Long userId);

    HotelBookings bookHotel(HotelBookingDTO hotelBookingDTO);

    Object bookingPayment(HotelBookings hotelBookings);
}
