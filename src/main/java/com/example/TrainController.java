package com.example;
import com.example.Train;
import com.example.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping("gettrains")
    public List<Train> getAllTrains() {
        return trainService.getAllTrains();
    }

    @GetMapping("gettrains/{tid}")
    public Train getTrainById(@PathVariable("tid") int tid) {
        return trainService.getTrainById(tid);
    }
}