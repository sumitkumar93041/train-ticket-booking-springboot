package com.example;



import com.example.Train;
import com.example.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    public List<Train> getAllTrains() {
        return (List<Train>) trainRepository.findAll();
    }

    public Train getTrainById(int id) {
        return trainRepository.findById(id).orElse(null);
    }
}