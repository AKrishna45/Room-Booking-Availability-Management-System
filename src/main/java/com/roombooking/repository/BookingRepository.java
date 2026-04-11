package com.roombooking.repository;

import com.roombooking.model.Booking;
import com.roombooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByIdDesc(User user);

    List<Booking> findAllByOrderByCheckInDesc();

    @Query("SELECT b.roomName, COUNT(b) FROM Booking b WHERE b.checkOut >= :today GROUP BY b.roomName")
    List<Object[]> countActiveBookingsByRoom(@Param("today") LocalDate today);
}
