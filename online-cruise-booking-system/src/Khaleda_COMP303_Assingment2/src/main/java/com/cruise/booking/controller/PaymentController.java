package com.cruise.booking.controller;

import com.cruise.booking.entity.PaymentTransaction;
import com.cruise.booking.service.BookingService;
import com.cruise.booking.service.PaymentTransactionService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentTransactionService paymentTransactionService;
    private final BookingService bookingService;

    public PaymentController(PaymentTransactionService paymentTransactionService, BookingService bookingService) {
        this.paymentTransactionService = paymentTransactionService;
        this.bookingService = bookingService;
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

    @GetMapping("/new/{bookingId}")
    public String showCreateForm(@PathVariable Long bookingId, Model model, RedirectAttributes ra) {
        return bookingService.getBookingById(bookingId).map(booking -> {
            PaymentTransaction payment = new PaymentTransaction();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
            payment.setCurrency("USD");
            
            model.addAttribute("payment", payment);
            model.addAttribute("booking", booking);
            return "payments/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Booking not found");
            return "redirect:/bookings";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute PaymentTransaction payment, @RequestParam Long bookingId, RedirectAttributes ra) {
        var booking = bookingService.getBookingById(bookingId).orElse(null);
        if (booking == null) {
            ra.addFlashAttribute("error", "Booking not found");
            return "redirect:/bookings";
        }
        
        payment.setBooking(booking);
        payment.setTransactionDate(LocalDateTime.now());
        payment.setTransactionStatus("COMPLETED");
        
        paymentTransactionService.saveTransaction(payment);
        
        // Update booking status to CONFIRMED after successful payment
        booking.setBookingStatus("CONFIRMED");
        bookingService.updateBooking(bookingId, booking);
        
        ra.addFlashAttribute("success", "Payment processed successfully. Booking status updated to CONFIRMED.");
        return "redirect:/bookings";
    }

    @GetMapping("/receipt/{transactionId}")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long transactionId) {
        var payment = paymentTransactionService.getTransactionById(transactionId).orElse(null);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            Paragraph title = new Paragraph("PAYMENT RECEIPT")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(title);

            // Receipt information
            document.add(new Paragraph("Receipt #: " + payment.getTransactionId())
                .setFontSize(12)
                .setBold());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            document.add(new Paragraph("Transaction Date: " + 
                (payment.getTransactionDate() != null ? payment.getTransactionDate().format(formatter) : "N/A"))
                .setFontSize(12)
                .setMarginBottom(20));

            // Customer and Booking Information
            if (payment.getBooking() != null) {
                document.add(new Paragraph("BOOKING INFORMATION")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10));
                
                Table bookingTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
                
                bookingTable.addCell("Booking ID:");
                bookingTable.addCell(String.valueOf(payment.getBooking().getBookingId()));
                
                if (payment.getBooking().getUser() != null) {
                    bookingTable.addCell("Customer Name:");
                    bookingTable.addCell(payment.getBooking().getUser().getFirstName() + " " + 
                        payment.getBooking().getUser().getLastName());
                    
                    bookingTable.addCell("Email:");
                    bookingTable.addCell(payment.getBooking().getUser().getEmail() != null ? 
                        payment.getBooking().getUser().getEmail() : "N/A");
                }
                
                if (payment.getBooking().getCruise() != null) {
                    bookingTable.addCell("Cruise:");
                    bookingTable.addCell(payment.getBooking().getCruise().getCruiseName() != null ? 
                        payment.getBooking().getCruise().getCruiseName() : "N/A");
                }
                
                bookingTable.addCell("Booking Status:");
                bookingTable.addCell(payment.getBooking().getBookingStatus());
                
                document.add(bookingTable);
            }

            // Payment Details
            document.add(new Paragraph("PAYMENT DETAILS")
                .setFontSize(14)
                .setBold()
                .setMarginTop(10));
            
            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);
            
            paymentTable.addCell("Amount:");
            paymentTable.addCell(String.format("$%.2f %s", 
                payment.getAmount(), 
                payment.getCurrency() != null ? payment.getCurrency() : "USD"));
            
            paymentTable.addCell("Payment Method:");
            paymentTable.addCell(payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "N/A");
            
            if (payment.getCardLastFour() != null) {
                paymentTable.addCell("Card (Last 4):");
                paymentTable.addCell("•••• " + payment.getCardLastFour());
            }
            
            paymentTable.addCell("Transaction Status:");
            paymentTable.addCell(payment.getTransactionStatus() != null ? payment.getTransactionStatus() : "N/A");
            
            if (payment.getPaymentGatewayRef() != null) {
                paymentTable.addCell("Gateway Reference:");
                paymentTable.addCell(payment.getPaymentGatewayRef());
            }
            
            document.add(paymentTable);

            // Footer
            document.add(new Paragraph("Thank you for your payment!")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30)
                .setItalic());

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payment-receipt-" + transactionId + ".pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .body(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
