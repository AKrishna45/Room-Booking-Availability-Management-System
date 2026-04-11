package com.roombooking.service;

import com.roombooking.dto.Dtos.BookingDto;
import com.roombooking.model.Booking;
import com.roombooking.model.User;
import com.roombooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking createBooking(BookingDto dto, User user) {
        String bookingId = "SE-" + (1000 + new Random().nextInt(9000));
        Booking booking = Booking.builder()
                .user(user)
                .roomName(dto.getRoomName())
                .checkIn(LocalDate.parse(dto.getCheckIn()))
                .checkOut(LocalDate.parse(dto.getCheckOut()))
                .guests(dto.getGuests())
                .mobile(dto.getMobile())
                .age(dto.getAge())
                .paymentType(dto.getPaymentType())
                .bookingId(bookingId)
                .status("confirmed")
                .build();
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByIdDesc(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCheckInDesc();
    }

    public void cancelBooking(Long id, User user) {
        bookingRepository.findById(id).ifPresent(b -> {
            if (b.getUser().getId().equals(user.getId())) {
                bookingRepository.delete(b);
            }
        });
    }

    public List<Object[]> getActiveBookingCountsByRoom() {
        return bookingRepository.countActiveBookingsByRoom(LocalDate.now());
    }
}
