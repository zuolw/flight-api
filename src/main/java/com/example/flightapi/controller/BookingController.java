package com.example.flightapi.controller;

import com.example.flightapi.dto.BookingRequestDTO;
import com.example.flightapi.dto.BookingResponseDTO;
import com.example.flightapi.service.BookingService;
import com.example.flightapi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.flightapi.dto.RoundTripBookingRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/{flightId}")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @PathVariable Long flightId,
            @RequestBody BookingRequestDTO bookingRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookingResponseDTO createdBooking = bookingService.createBooking(flightId, bookingRequest);
        return ResponseEntity.ok(createdBooking);
    }

    @PostMapping("/roundtrip")
    public ResponseEntity<List<BookingResponseDTO>> createRoundTripBooking(
            @RequestBody RoundTripBookingRequestDTO roundTripBookingRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserIdFromUserDetails(userDetails);
        List<BookingResponseDTO> createdBookings = bookingService.createRoundTripBooking(userId, roundTripBookingRequest);
        return ResponseEntity.ok(createdBookings);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("Getting bookings for user: {}", userDetails.getUsername());
        String email = getEmailFromUserDetails(userDetails);
        logger.debug("User email: {}", email);
        List<BookingResponseDTO> bookings = bookingService.getUserBookings(email);
        logger.debug("Found {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingNumber}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable String bookingNumber) {
        BookingResponseDTO booking = bookingService.getBookingByNumber(bookingNumber);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{bookingNumber}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingNumber) {
        bookingService.cancelBooking(bookingNumber);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) userDetails).getUser().getId();
        }
        throw new IllegalArgumentException("UserDetails is not an instance of UserDetailsImpl");
    }

    private String getEmailFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) userDetails).getUsername();
        }
        throw new IllegalArgumentException("UserDetails is not an instance of UserDetailsImpl");
    }
} 