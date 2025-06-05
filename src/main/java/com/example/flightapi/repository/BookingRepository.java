package com.example.flightapi.repository;

import com.example.flightapi.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN FETCH b.flight WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);
    
    Optional<Booking> findByBookingNumber(String bookingNumber);
    
    @Query("SELECT b FROM Booking b JOIN FETCH b.flight WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    boolean existsByBookingNumber(String bookingNumber);
}
