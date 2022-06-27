package com.horse.data.repository.trainer;

import com.horse.data.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Integer>, TrainerCustomRepository {
}