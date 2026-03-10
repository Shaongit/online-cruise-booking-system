/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.controller;

import com.cruise.booking.entity.User;
import com.cruise.booking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Controller for authentication operations.
 * Handles user login and registration functionality.
 * Provides endpoints for the login and registration pages.
 */
@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("error", "Invalid email or password. Please try again.");
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {

        if (!user.getPasswordHash().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "auth/register";
        }
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "An account with this email already exists.");
            return "auth/register";
        }
        if (user.getPasswordHash().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "auth/register";
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userService.saveUser(user);

        // Re-use model attribute for the redirect (not flash because we return view directly)
        model.addAttribute("user", new User());
        model.addAttribute("success", "Registration successful! Please log in.");
        return "auth/login";
    }
}
