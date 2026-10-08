package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    private LocalDate travelDate;
    private int seatNumber;
    private String passengerName;
    private int age;
    private String phone;
    private int fare;
    private String status;
    private int refundAmount;

    public Booking() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getFare() { return fare; }
    public void setFare(int fare) { this.fare = fare; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRefundAmount() { return refundAmount; }
    public void setRefundAmount(int refundAmount) { this.refundAmount = refundAmount; }
    
}