package com.cruise.booking.controller;

import com.cruise.booking.service.PaymentTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentTransactionService paymentTransactionService;

    public PaymentController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("payments", paymentTransactionService.getAllTransactions());
        return "payments/list";
    }

    @GetMapping("/booking/{bookingId}")
    public String listByBooking(@PathVariable Long bookingId, Model model) {
        model.addAttribute("payments", paymentTransactionService.getTransactionsByBookingId(bookingId));
        model.addAttribute("bookingId", bookingId);
        return "payments/list";
    }
}
