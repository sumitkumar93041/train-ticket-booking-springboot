package com.example;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("createbooking")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking saved = bookingService.createBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("getbookings")
    public List<Booking> getBookings(@RequestParam(value = "phone", required = false) String phone) {
        if (phone != null) {
            return bookingService.getBookingsByPhone(phone);
        }
        return bookingService.getAllBookings();
    }

    @GetMapping("getbookings/{bid}")
    public Booking getBookingById(@PathVariable("bid") int bid) {
        return bookingService.getBookingById(bid);
    }
    @PostMapping("cancelbooking/{bid}")
    public ResponseEntity<?> cancelBooking(@PathVariable("bid") int bid, @RequestBody Map<String, String> body) {
        try {
            Booking cancelled = bookingService.cancelBooking(bid, body.get("phone"));
            return ResponseEntity.ok(cancelled);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}