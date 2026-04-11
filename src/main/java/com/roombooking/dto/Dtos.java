package com.roombooking.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class Dtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDto {
        @NotBlank(message = "Full name is required")
        private String fullname;

        @Email(message = "Valid email is required")
        @NotBlank
        private String email;

        @NotBlank
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "Password must be 8+ chars with uppercase, lowercase, number, and special character."
        )
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        @Email @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDto {
        @NotBlank
        private String roomName;
        @NotBlank
        private String checkIn;
        @NotBlank
        private String checkOut;
        @Min(1) @Max(4)
        private Integer guests;
        @Pattern(regexp = "[0-9]{10}", message = "Enter a valid 10-digit mobile number")
        private String mobile;
        @Min(18)
        private Integer age;
        private String paymentType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDto {
        @NotBlank
        private String roomType;
        @Min(1)
        private Integer totalRooms;
        @Min(1)
        private Integer price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminProfileDto {
        @Email @NotBlank
        private String adminEmail;
        private String adminPassword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VacancyInfo {
        private int available;
        private boolean soldOut;
    }
}
