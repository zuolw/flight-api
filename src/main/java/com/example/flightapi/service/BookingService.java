package com.example.flightapi.service;

import com.example.flightapi.dto.BookingRequestDTO;
import com.example.flightapi.dto.BookingResponseDTO;
import com.example.flightapi.entity.Booking;
import com.example.flightapi.entity.Flight;
import com.example.flightapi.entity.User;
import com.example.flightapi.repository.BookingRepository;
import com.example.flightapi.repository.FlightRepository;
import com.example.flightapi.repository.UserRepository;
import com.example.flightapi.dto.RoundTripBookingRequestDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final FlightService flightService;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Transactional
    public BookingResponseDTO createBooking(Long flightId, BookingRequestDTO requestDTO) {
        // 验证参数
        if (requestDTO.getPassengerName() == null || requestDTO.getPassengerName().trim().isEmpty()) {
            throw new IllegalArgumentException("乘客姓名不能为空");
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("航班不存在"));

        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setPassengerName(requestDTO.getPassengerName().trim());
        booking.setStatus("PENDING");
        booking.setTotalPrice(flight.getPrice());
        booking.setBookingNumber(generateBookingNumber());
        booking.setBookingTime(LocalDateTime.now());

        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    @Transactional
    public List<BookingResponseDTO> createRoundTripBooking(Long userId, RoundTripBookingRequestDTO roundTripBookingRequest) {
        // Handle outbound booking
        BookingRequestDTO outboundPassengerInfo = roundTripBookingRequest.getPassengerInfo();
        Long outboundFlightId = roundTripBookingRequest.getOutboundFlightId();
        BookingResponseDTO outboundBooking = createBooking(outboundFlightId, outboundPassengerInfo);

        // Handle return booking
        BookingRequestDTO returnPassengerInfo = roundTripBookingRequest.getPassengerInfo(); // Assuming same passenger info for return
        Long returnFlightId = roundTripBookingRequest.getReturnFlightId();
        BookingResponseDTO returnBooking = createBooking(returnFlightId, returnPassengerInfo);

        // Return a list containing both bookings
        return List.of(outboundBooking, returnBooking);
    }

    public List<BookingResponseDTO> getUserBookings(String email) {
        logger.debug("Getting bookings for user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new IllegalArgumentException("用户不存在");
                });
        
        logger.debug("Found user with ID: {}", user.getId());
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        logger.debug("Found {} bookings for user", bookings.size());
        
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingByNumber(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("预订不存在"));
        return convertToDTO(booking);
    }

    @Transactional
    public void cancelBooking(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("预订不存在"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("预订已取消");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // 恢复航班座位
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
    }

    private String generateBookingNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setBookingNumber(booking.getBookingNumber());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setAirline(booking.getFlight().getAirline());
        dto.setOrigin(booking.getFlight().getOrigin());
        dto.setDestination(booking.getFlight().getDestination());
        
        // 将LocalTime转换为LocalDateTime
        LocalDateTime departureDateTime = LocalDateTime.of(
            booking.getFlight().getDepartureDate(),
            booking.getFlight().getDepartureTime()
        );
        LocalDateTime arrivalDateTime = LocalDateTime.of(
            booking.getFlight().getDepartureDate(),
            booking.getFlight().getArrivalTime()
        );
        
        dto.setDepartureTime(departureDateTime);
        dto.setArrivalTime(arrivalDateTime);
        dto.setPassengerName(booking.getPassengerName());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setBookingTime(booking.getBookingTime());
        return dto;
    }
}
