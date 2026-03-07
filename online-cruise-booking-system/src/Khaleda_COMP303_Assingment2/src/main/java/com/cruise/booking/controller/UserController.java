package com.cruise.booking.controller;

import com.cruise.booking.entity.User;
import com.cruise.booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        if (user.getUserId() != null) {
            userService.updateUser(user.getUserId(), user);
            ra.addFlashAttribute("success", "User updated successfully.");
        } else {
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
