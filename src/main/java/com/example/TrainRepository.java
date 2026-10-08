package com.example;


import com.example.Train;
import org.springframework.data.repository.CrudRepository;

public interface TrainRepository extends CrudRepository<Train, Integer> {
}