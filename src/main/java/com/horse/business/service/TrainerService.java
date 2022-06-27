package com.horse.business.service;

import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;

public interface TrainerService {

    TrainerResponse createTrainer(TrainerRequest trainerRequest);
    TrainerResponse updateTrainer(Integer trainerId, TrainerRequest trainerRequest);
    void deleteTrainer(Integer id);
    TrainerResponse getOne(Integer id);
}
