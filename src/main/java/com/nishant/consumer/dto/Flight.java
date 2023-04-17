package com.nishant.consumer.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long flightId;

    private String name;

    private String departureLocation;

    private String arrivalLocation;

    private String flightClass;

    private LocalDate flightDate;

    private int seatsAvailable;

    private Long price;

    public Flight(String name) {
        this.name =name;
    }
}
