package com.example.flightapi.dto;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String passengerName;
    private String email; // Assuming email is also sent with passenger info
} 