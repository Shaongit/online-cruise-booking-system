package com.cruise.booking.controller;

import com.cruise.booking.entity.Passenger;
import com.cruise.booking.service.BookingService;
import com.cruise.booking.service.PassengerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;
    private final BookingService bookingService;

    public PassengerController(PassengerService passengerService, BookingService bookingService) {
        this.passengerService = passengerService;
        this.bookingService = bookingService;
    }

    @GetMapping("/booking/{bookingId}/new")
    public String showCreateForm(@PathVariable Long bookingId, Model model, RedirectAttributes ra) {
        return bookingService.getBookingById(bookingId).map(booking -> {
            Passenger passenger = new Passenger();
            passenger.setBooking(booking);
            model.addAttribute("passenger", passenger);
            model.addAttribute("booking", booking);
            return "passengers/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Booking not found");
            return "redirect:/bookings";
        });
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return passengerService.getPassengerById(id).map(passenger -> {
            model.addAttribute("passenger", passenger);
            model.addAttribute("booking", passenger.getBooking());
            return "passengers/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Passenger not found");
            return "redirect:/bookings";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Passenger passenger,
                      @RequestParam Long bookingId,
                      RedirectAttributes ra) {
        return bookingService.getBookingById(bookingId).map(booking -> {
            passenger.setBooking(booking);
            
            if (passenger.getPassengerId() != null) {
                passengerService.updatePassenger(passenger.getPassengerId(), passenger);
                ra.addFlashAttribute("success", "Passenger updated successfully.");
            } else {
                passengerService.savePassenger(passenger);
                ra.addFlashAttribute("success", "Passenger added successfully.");
            }
            
            // Update booking's numberOfPassengers and totalPrice
            int passengerCount = passengerService.getPassengersByBookingId(bookingId).size();
            booking.setNumberOfPassengers(passengerCount);
            
            // Recalculate total price based on cruise base price and passenger count
            if (booking.getCruise() != null && booking.getCruise().getBasePrice() != null) {
                booking.setTotalPrice(
                    booking.getCruise().getBasePrice()
                        .multiply(java.math.BigDecimal.valueOf(passengerCount))
                );
            }
            
            bookingService.updateBooking(bookingId, booking);
            
            return "redirect:/bookings/" + bookingId + "/edit";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Booking not found");
            return "redirect:/bookings";
        });
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        Passenger passenger = passengerService.getPassengerById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        Long bookingId = passenger.getBooking().getBookingId();
        
        passengerService.deletePassenger(id);
        
        // Update booking's numberOfPassengers and totalPrice
        bookingService.getBookingById(bookingId).ifPresent(booking -> {
            int passengerCount = passengerService.getPassengersByBookingId(bookingId).size();
            booking.setNumberOfPassengers(passengerCount);
            
            // Recalculate total price based on cruise base price and passenger count
            if (booking.getCruise() != null && booking.getCruise().getBasePrice() != null) {
                booking.setTotalPrice(
                    booking.getCruise().getBasePrice()
                        .multiply(java.math.BigDecimal.valueOf(passengerCount))
                );
            }
            
            bookingService.updateBooking(bookingId, booking);
        });
        
        ra.addFlashAttribute("success", "Passenger removed successfully.");
        return "redirect:/bookings/" + bookingId + "/edit";
    }
}
