package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String trainNumber;
    private String name;
    private String source;
    private String destination;
    private int totalSeats;
    private int fare;

    public Train() {
    }

    public Train(String trainNumber, String name, String source, String destination, int totalSeats, int fare) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.fare = fare;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getFare() { return fare; }
    public void setFare(int fare) { this.fare = fare; }
}