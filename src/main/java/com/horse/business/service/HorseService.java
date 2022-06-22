package com.horse.business.service;

import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;

import java.util.List;

public interface HorseService {

    List<HorseResponse> findAll();
    List<HorseResponse> findByTrainerAndDate(Integer TrainerId, Integer year);
    HorseResponse getOne(Integer id);
    HorseResponse createHorse(HorseRequest horseRequest);
    HorseResponse updateHorse(Integer id, HorseRequest horseRequest);
    void deleteHorse(Integer id);
}
