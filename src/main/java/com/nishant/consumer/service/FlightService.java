package com.nishant.consumer.service;

import com.nishant.consumer.dto.Flight;
import com.nishant.consumer.dto.FlightBookingRequest;
import com.nishant.consumer.dto.FlightBookings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightService {

    String searchFlights(Flight flight);

    List<Flight> findAllFlights();

    Object bookFlight(FlightBookingRequest flightBookingRequest);

    Object bookingPayment(FlightBookings flightBookings);
}
