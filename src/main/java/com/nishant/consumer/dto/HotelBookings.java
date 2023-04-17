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
public class HotelBookings implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Hotel hotel;

    private Long userId;

    private String status;

    private String roomNumber;
}
