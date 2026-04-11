package com.roombooking.controller;

import com.roombooking.dto.Dtos.RegisterDto;
import com.roombooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ── Login ──
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        return "auth/login";
    }

    // ── Register ──
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto dto,
                           BindingResult result, Model model) {
        if (result.hasErrors()) return "auth/register";

        if (userService.emailExists(dto.getEmail())) {
            model.addAttribute("error", "Email already in use. Please login.");
            return "auth/register";
        }
        userService.register(dto);
        return "redirect:/login?registered=true";
    }
}
