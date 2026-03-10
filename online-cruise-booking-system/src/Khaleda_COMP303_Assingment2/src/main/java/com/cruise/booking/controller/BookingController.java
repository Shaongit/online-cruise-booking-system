/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.controller;

import com.cruise.booking.entity.Booking;
import com.cruise.booking.service.BookingService;
import com.cruise.booking.service.CruiseCabinService;
import com.cruise.booking.service.CruiseService;
import com.cruise.booking.service.PassengerService;
import com.cruise.booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for booking management operations.
 * Handles creating, updating, and viewing cruise bookings.
 * Manages booking details including passengers, cabins, and pricing.
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final CruiseService cruiseService;
    private final CruiseCabinService cruiseCabinService;
    private final PassengerService passengerService;

    public BookingController(BookingService bookingService, UserService userService,
                                CruiseService cruiseService, CruiseCabinService cruiseCabinService,
                                PassengerService passengerService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.cruiseService = cruiseService;
        this.cruiseCabinService = cruiseCabinService;
        this.passengerService = passengerService;
    }

    @GetMapping
    public String list(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        
        // Add cancellation eligibility information for each booking
        java.util.Map<Long, Boolean> cancellationEligibility = new java.util.HashMap<>();
        java.util.Map<Long, Long> daysUntilDeparture = new java.util.HashMap<>();
        
        for (Booking booking : bookings) {
            cancellationEligibility.put(booking.getBookingId(), bookingService.canCancelBooking(booking.getBookingId()));
            daysUntilDeparture.put(booking.getBookingId(), bookingService.getDaysUntilDeparture(booking.getBookingId()));
        }
        
        model.addAttribute("cancellationEligibility", cancellationEligibility);
        model.addAttribute("daysUntilDeparture", daysUntilDeparture);
        
        return "bookings/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("cruises", cruiseService.getAvailableCruises());
        model.addAttribute("cabins", cruiseCabinService.getAllCruiseCabins());
        return "bookings/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return bookingService.getBookingById(id).map(booking -> {
            // Load passengers for this booking
            booking.setPassengers(passengerService.getPassengersByBookingId(id));
            
            model.addAttribute("booking", booking);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("cruises", cruiseService.getAvailableCruises());
            model.addAttribute("cabins", cruiseCabinService.getAllCruiseCabins());
            return "bookings/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Booking not found");
            return "redirect:/bookings";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Booking booking, RedirectAttributes ra) {
        if (booking.getBookingId() != null) {
            // Fetch full cruise object from database
            if (booking.getCruise() != null && booking.getCruise().getCruiseId() != null) {
                var cruise = cruiseService.getCruiseById(booking.getCruise().getCruiseId()).orElse(null);
                booking.setCruise(cruise);
                
                // Recalculate total price based on cruise base price and number of passengers
                if (cruise != null && booking.getNumberOfPassengers() != null) {
                    booking.setTotalPrice(
                        cruise.getBasePrice()
                            .multiply(java.math.BigDecimal.valueOf(booking.getNumberOfPassengers()))
                    );
                }
            }
            bookingService.updateBooking(booking.getBookingId(), booking);
            ra.addFlashAttribute("success", "Booking updated successfully.");
            return "redirect:/bookings/" + booking.getBookingId() + "/edit";
        } else {
            // Set default status to PENDING for new bookings
            booking.setBookingStatus("PENDING");
            // Set initial number of passengers to 0
            booking.setNumberOfPassengers(0);
            
            // Fetch full cruise object from database and set initial total price to cruise base price
            if (booking.getCruise() != null && booking.getCruise().getCruiseId() != null) {
                var cruise = cruiseService.getCruiseById(booking.getCruise().getCruiseId()).orElse(null);
                booking.setCruise(cruise);
                if (cruise != null && cruise.getBasePrice() != null) {
                    booking.setTotalPrice(cruise.getBasePrice());
                } else {
                    booking.setTotalPrice(java.math.BigDecimal.ZERO);
                }
            } else {
                booking.setTotalPrice(java.math.BigDecimal.ZERO);
            }
            
            Booking savedBooking = bookingService.saveBooking(booking);
            ra.addFlashAttribute("success", "Booking created successfully. Now add passengers.");
            return "redirect:/bookings/" + savedBooking.getBookingId() + "/edit";
        }
    }

    @GetMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes ra) {
        try {
            // Check if cancellation is allowed before attempting
            if (!bookingService.canCancelBooking(id)) {
                long daysUntilDeparture = bookingService.getDaysUntilDeparture(id);
                if (daysUntilDeparture < 0) {
                    ra.addFlashAttribute("error", "Cannot cancel a booking for a cruise that has already departed.");
                } else if (daysUntilDeparture < 10) {
                    ra.addFlashAttribute("error", 
                        String.format("Cancellation not allowed. You can only cancel up to 10 days before departure. " +
                                    "Your cruise departs in %d day(s).", daysUntilDeparture));
                } else {
                    ra.addFlashAttribute("error", "Booking cannot be cancelled.");
                }
                return "redirect:/bookings";
            }
            
            // Proceed with cancellation
            bookingService.cancelBooking(id);
            ra.addFlashAttribute("success", "Booking cancelled successfully.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "An error occurred while cancelling the booking.");
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        bookingService.deleteBooking(id);
        ra.addFlashAttribute("success", "Booking deleted.");
        return "redirect:/bookings";
    }
}
