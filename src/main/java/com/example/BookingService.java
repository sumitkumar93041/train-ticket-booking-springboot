package com.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TrainRepository trainRepository;

    public Booking createBooking(Booking booking) {
        Train train = trainRepository.findById(booking.getTrain().getId()).orElse(null);
        if (train == null) {
            throw new RuntimeException("Train not found");
        }

        Optional<Booking> existing = bookingRepository.findByTrain_IdAndTravelDateAndSeatNumberAndStatus(
                train.getId(), booking.getTravelDate(), booking.getSeatNumber(), "BOOKED");
        if (existing.isPresent()) {
            throw new RuntimeException("Seat " + booking.getSeatNumber() + " is already booked for that date");
        }

        booking.setTrain(train);
        booking.setFare(train.getFare());
        booking.setStatus("BOOKED");
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return (List<Booking>) bookingRepository.findAll();
    }

    public Booking getBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsByPhone(String phone) {
        return bookingRepository.findByPhone(phone);
    }
    
    public Booking cancelBooking(int id, String phone) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null || phone == null || !booking.getPhone().equals(phone)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Booking is already cancelled");
        }

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), booking.getTravelDate());
        int percent = (daysLeft >= 2) ? 75 : 50;

        booking.setRefundAmount(booking.getFare() * percent / 100);
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}