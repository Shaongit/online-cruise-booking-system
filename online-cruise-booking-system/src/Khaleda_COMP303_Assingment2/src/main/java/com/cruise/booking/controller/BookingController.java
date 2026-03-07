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
        model.addAttribute("bookings", bookingService.getAllBookings());
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
        bookingService.cancelBooking(id);
        ra.addFlashAttribute("success", "Booking cancelled.");
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        bookingService.deleteBooking(id);
        ra.addFlashAttribute("success", "Booking deleted.");
        return "redirect:/bookings";
    }
}
