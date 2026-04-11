package com.roombooking.controller;

import com.roombooking.dto.Dtos.VacancyInfo;
import com.roombooking.model.Room;
import com.roombooking.service.BookingService;
import com.roombooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final RoomService roomService;
    private final BookingService bookingService;

    // Replaces api_vacancies.php
    @GetMapping("/vacancies")
    public Map<String, VacancyInfo> getVacancies() {
        List<Room> rooms = roomService.getAllRooms();
        List<Object[]> activeBookings = bookingService.getActiveBookingCountsByRoom();

        // Build a map: roomType -> bookingCount
        Map<String, Long> bookingMap = new HashMap<>();
        for (Object[] row : activeBookings) {
            bookingMap.put((String) row[0], (Long) row[1]);
        }

        Map<String, VacancyInfo> response = new HashMap<>();
        for (Room room : rooms) {
            long booked = bookingMap.getOrDefault(room.getRoomType(), 0L);
            int available = room.getTotalRooms() - (int) booked;
            String id = room.getRoomType().toLowerCase().replace(" ", "-");
            response.put(id, new VacancyInfo(Math.max(available, 0), available <= 0));
        }
        return response;
    }
}
