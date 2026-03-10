/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.controller;

import com.cruise.booking.service.BookingService;
import com.cruise.booking.service.CruiseService;
import com.cruise.booking.service.ShipService;
import com.cruise.booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home/dashboard page.
 * Displays system statistics and recent activities.
 * Provides an overview of cruises, bookings, users, and ships.
 */
@Controller
public class HomeController {

    private final CruiseService cruiseService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ShipService shipService;

    public HomeController(CruiseService cruiseService, BookingService bookingService,
                             UserService userService, ShipService shipService) {
        this.cruiseService = cruiseService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.shipService = shipService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cruiseCount", cruiseService.getAllCruises().size());
        model.addAttribute("bookingCount", bookingService.getAllBookings().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("shipCount", shipService.getAllShips().size());
        model.addAttribute("recentBookings", bookingService.getAllBookings());
        model.addAttribute("availableCruises", cruiseService.getAvailableCruises());
        return "index";
    }
}
