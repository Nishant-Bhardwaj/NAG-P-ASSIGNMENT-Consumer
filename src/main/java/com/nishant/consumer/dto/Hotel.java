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
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long hotelId;

    private String name;

    private String city;

    private int roomsAvailable;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long price;

    public Hotel(String name) {
        this.name =name;
    }
}
