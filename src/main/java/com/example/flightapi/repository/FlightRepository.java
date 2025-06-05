package com.example.flightapi.repository;

import com.example.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginAndDestinationAndDepartureTimeBetween(
        String origin, 
        String destination, 
        LocalDateTime startTime, 
        LocalDateTime endTime
    );

    @Query("SELECT f FROM Flight f WHERE f.origin = ?1 AND f.destination = ?2 AND f.departureDate >= ?3")
    List<Flight> findAvailableFlights(String departureCode, String destinationCode, LocalDate departureDate);

    boolean existsByFlightNumber(String flightNumber);
}
