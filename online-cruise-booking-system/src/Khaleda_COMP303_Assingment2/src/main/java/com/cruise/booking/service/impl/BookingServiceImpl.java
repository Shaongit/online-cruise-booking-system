/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.Booking;
import com.cruise.booking.repository.BookingRepository;
import com.cruise.booking.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of BookingService interface.
 * Provides business logic for managing cruise bookings.
 * Handles booking creation, updates, cancellation, and retrieval operations.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserUserId(userId);
    }

    @Override
    public List<Booking> getBookingsByCruiseId(Long cruiseId) {
        return bookingRepository.findByCruiseCruiseId(cruiseId);
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        booking.setBookingId(id);
        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long id) throws IllegalStateException {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Booking not found"));
        
        // Check if the booking is already cancelled
        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        
        // Validate cancellation policy: Must be at least 10 days before departure
        if (!canCancelBooking(id)) {
            long daysUntilDeparture = getDaysUntilDeparture(id);
            if (daysUntilDeparture < 0) {
                throw new IllegalStateException("Cannot cancel a booking for a cruise that has already departed");
            } else {
                throw new IllegalStateException(
                    String.format("Cancellation not allowed. You can only cancel up to 10 days before departure. " +
                                "Your cruise departs in %d day(s).", daysUntilDeparture)
                );
            }
        }
        
        // Proceed with cancellation
        booking.setBookingStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    @Override
    public boolean canCancelBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            return false;
        }
        
        Booking booking = bookingOpt.get();
        
        // Check if booking is already cancelled
        if ("CANCELLED".equals(booking.getBookingStatus())) {
            return false;
        }
        
        // Check if cruise and departure date exist
        if (booking.getCruise() == null || booking.getCruise().getDepartureDate() == null) {
            return false;
        }
        
        LocalDate departureDate = booking.getCruise().getDepartureDate();
        LocalDate today = LocalDate.now();
        
        // Calculate days until departure
        long daysUntilDeparture = ChronoUnit.DAYS.between(today, departureDate);
        
        // Allow cancellation if at least 10 days before departure
        return daysUntilDeparture >= 10;
    }

    @Override
    public long getDaysUntilDeparture(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            return -1;
        }
        
        Booking booking = bookingOpt.get();
        if (booking.getCruise() == null || booking.getCruise().getDepartureDate() == null) {
            return -1;
        }
        
        LocalDate departureDate = booking.getCruise().getDepartureDate();
        LocalDate today = LocalDate.now();
        
        return ChronoUnit.DAYS.between(today, departureDate);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
