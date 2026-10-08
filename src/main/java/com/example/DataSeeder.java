package com.example;



import com.example.Train;
import com.example.TrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TrainRepository trainRepository;

    public DataSeeder(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public void run(String... args) {
        if (trainRepository.count() == 0) {
            trainRepository.save(new Train("T101", "Karnataka Express", "Bengaluru", "Mangaluru", 20, 650));
            trainRepository.save(new Train("T102", "Mysuru Shatabdi", "Bengaluru", "Mysuru", 16, 300));
            trainRepository.save(new Train("T103", "Coastal Superfast", "Mangaluru", "Chennai", 12, 900));
        }
    }
}