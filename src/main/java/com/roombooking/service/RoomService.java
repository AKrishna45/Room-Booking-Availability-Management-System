package com.roombooking.service;

import com.roombooking.dto.Dtos.RoomDto;
import com.roombooking.model.Room;
import com.roombooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void saveOrUpdateRoom(RoomDto dto) {
        Room room = roomRepository.findByRoomType(dto.getRoomType())
                .orElse(Room.builder().roomType(dto.getRoomType()).build());
        room.setTotalRooms(dto.getTotalRooms());
        room.setPrice(dto.getPrice());
        roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
