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
 * Controller for user management operations.
 * Handles CRUD operations for user accounts.
 * Manages user registration, updates, and password encoding.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return userService.getUserById(id).map(user -> {
            model.addAttribute("user", user);
            return "users/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "User not found");
            return "redirect:/users";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user, RedirectAttributes ra) {
        boolean isUpdate = user.getUserId() != null;
        if (isUpdate) {
            userService.getUserById(user.getUserId()).ifPresent(existing -> {
                if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
                    user.setPasswordHash(existing.getPasswordHash());
                } else {
                    user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
                }
                if (user.getRole() == null || user.getRole().isBlank()) {
                    user.setRole(existing.getRole());
                }
                user.setCreatedAt(existing.getCreatedAt());
            });
            user.setUpdatedAt(LocalDateTime.now());
            userService.updateUser(user.getUserId(), user);
            ra.addFlashAttribute("success", "User updated successfully.");
        } else {
            if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
                user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            if (user.getRole() == null || user.getRole().isBlank()) user.setRole("ROLE_USER");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userService.saveUser(user);
            ra.addFlashAttribute("success", "User created successfully.");
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/users";
    }
}
