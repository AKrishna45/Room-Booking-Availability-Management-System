package com.roombooking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false, unique = true, length = 100)
    private String roomType;

    @Column(name = "total_rooms", nullable = false)
    private Integer totalRooms;

    @Column(nullable = false)
    private Integer price;
}
