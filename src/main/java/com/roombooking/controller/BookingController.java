package com.roombooking.controller;

import com.roombooking.dto.Dtos.BookingDto;
import com.roombooking.model.Booking;
import com.roombooking.model.User;
import com.roombooking.service.BookingService;
import com.roombooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    // ── GET booking form (booking.php) ──
    @GetMapping("/booking")
    public String bookingForm(@RequestParam(defaultValue = "Deluxe Room") String room,
                              Model model) {
        model.addAttribute("room", room);
        model.addAttribute("bookingDto", new BookingDto());
        return "user/booking";
    }

    // ── POST booking form ──
    @PostMapping("/booking")
    public String submitBooking(@Valid @ModelAttribute("bookingDto") BookingDto dto,
                                BindingResult result,
                                Authentication auth,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("room", dto.getRoomName());
            return "user/booking";
        }
        if (dto.getGuests() != null && dto.getGuests() > 4) {
            model.addAttribute("error", "Sorry, a maximum of 4 members are allowed per room.");
            model.addAttribute("room", dto.getRoomName());
            return "user/booking";
        }
        User user = userService.findByEmail(auth.getName());
        bookingService.createBooking(dto, user);
        redirectAttributes.addFlashAttribute("success", "Booking confirmed! Booking ID generated.");
        return "redirect:/my-bookings";
    }

    // ── My Bookings (my-bookings.php) ──
    @GetMapping("/my-bookings")
    public String myBookings(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        List<Booking> bookings = bookingService.getUserBookings(user);
        model.addAttribute("bookings", bookings);
        return "user/my-bookings";
    }

    // ── Cancel Booking (cancel-booking.php) ──
    @GetMapping("/cancel-booking/{id}")
    public String cancelBooking(@PathVariable Long id, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        bookingService.cancelBooking(id, user);
        return "redirect:/my-bookings";
    }
}
