package com.example;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

	Optional<Booking> findByTrain_IdAndTravelDateAndSeatNumberAndStatus(int trainId, LocalDate travelDate, int seatNumber, String status);

    List<Booking> findByPhone(String phone);
}