package com.roombooking.controller;

import com.roombooking.dto.Dtos.AdminProfileDto;
import com.roombooking.dto.Dtos.RoomDto;
import com.roombooking.model.Admin;
import com.roombooking.repository.AdminRepository;
import com.roombooking.service.BookingService;
import com.roombooking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Admin Login page ──
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Invalid admin credentials.");
        return "admin/login";
    }

    // ── Admin Dashboard (admin.php) ──
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("adminEmail", auth.getName());
        model.addAttribute("roomDto", new RoomDto());
        model.addAttribute("profileDto", new AdminProfileDto());
        return "admin/dashboard";
    }

    // ── Add / Update Room ──
    @PostMapping("/room/save")
    public String saveRoom(@Valid @ModelAttribute RoomDto dto,
                           RedirectAttributes redirectAttributes) {
        roomService.saveOrUpdateRoom(dto);
        redirectAttributes.addFlashAttribute("roomSuccess", "Room saved successfully.");
        return "redirect:/admin/dashboard";
    }

    // ── Delete Room ──
    @PostMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        roomService.deleteRoom(id);
        redirectAttributes.addFlashAttribute("roomSuccess", "Room deleted.");
        return "redirect:/admin/dashboard";
    }

    // ── Update Admin Profile ──
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute AdminProfileDto dto,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Admin admin = adminRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (dto.getAdminEmail() != null && !dto.getAdminEmail().isBlank()) {
            admin.setEmail(dto.getAdminEmail());
        }

        if (dto.getAdminPassword() != null && !dto.getAdminPassword().isBlank()) {
            String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
            if (!dto.getAdminPassword().matches(pattern)) {
                redirectAttributes.addFlashAttribute("profileError",
                        "Weak password! Must be 8+ chars with uppercase, number, and special char.");
                return "redirect:/admin/dashboard";
            }
            admin.setPassword(passwordEncoder.encode(dto.getAdminPassword()));
        }
        adminRepository.save(admin);
        redirectAttributes.addFlashAttribute("profileSuccess", "Profile updated successfully!");
        return "redirect:/admin/dashboard";
    }
}
