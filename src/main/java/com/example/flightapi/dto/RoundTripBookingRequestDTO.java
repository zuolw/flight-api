package com.example.flightapi.dto;

import lombok.Data;

@Data
public class RoundTripBookingRequestDTO {
    private Long outboundFlightId;
    private Long returnFlightId;
    private BookingRequestDTO passengerInfo; // Reuse the passenger info DTO

    // Getters and Setters
} 