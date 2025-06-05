package com.example.flightapi.service;

import com.example.flightapi.entity.Flight;
import com.example.flightapi.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    public List<Flight> searchFlights(String departureCode, String destinationCode, LocalDate departureDate) {
        return flightRepository.findAvailableFlights(departureCode, destinationCode, departureDate);
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("航班不存在"));
    }

    @Transactional
    public Flight updateAvailableSeats(Long flightId, int seatsToBook) {
        Flight flight = getFlightById(flightId);
        if (flight.getAvailableSeats() < seatsToBook) {
            throw new RuntimeException("可用座位不足");
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - seatsToBook);
        return flightRepository.save(flight);
    }

    public boolean isFlightAvailable(Long flightId, int seatsToBook) {
        Flight flight = getFlightById(flightId);
        return flight.getAvailableSeats() >= seatsToBook;
    }
}
