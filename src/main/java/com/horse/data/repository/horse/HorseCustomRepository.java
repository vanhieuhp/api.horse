package com.horse.data.repository.horse;

import com.horse.data.entity.Horse;

import java.util.List;

public interface HorseCustomRepository {

    List<Horse> findByTrainerIdAndYear(Integer trainerId, Integer year);
}
