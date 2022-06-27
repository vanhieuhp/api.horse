package com.horse.business.impl;

import com.horse.business.service.HorseService;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;
import com.horse.data.entity.Horse;
import com.horse.data.entity.Trainer;
import com.horse.data.repository.horse.HorseRepository;
import com.horse.data.repository.trainer.TrainerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorseServiceImpl implements HorseService {

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<HorseResponse> findAll() {

        return horseRepository.findAll().stream().map(horse -> {
            HorseResponse horseResponse = mapper.map(horse, HorseResponse.class);
            horseResponse.setTrainerId(horse.getTrainer().getId());
            horseResponse.setTrainerName(horse.getTrainer().getName());
            return horseResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HorseResponse> findByTrainerAndDate(Integer trainerId, Integer year) {

        return horseRepository.findByTrainerIdAndYear(trainerId, year).stream().map(horse -> {

            HorseResponse horseResponse = mapper.map(horse, HorseResponse.class);
            horseResponse.setTrainerId(horse.getTrainer().getId());
            horseResponse.setTrainerName(horse.getTrainer().getName());

            return horseResponse;

        }).collect(Collectors.toList());
    }

    @Override
    public HorseResponse getOne(Integer id) {

        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Horse not found with id: " + id));

        // convert data into horseResponse
        HorseResponse horseResponse = mapper.map(horse, HorseResponse.class);
        horseResponse.setTrainerId(horse.getTrainer().getId());
        horseResponse.setTrainerName(horse.getTrainer().getName());

        return horseResponse;
    }

    @Override
    @Transactional
    public HorseResponse createHorse(HorseRequest horseRequest) {

        Trainer trainer = trainerRepository.findById(horseRequest.getTrainerId())
                .orElseThrow(() -> new GeneralExceptionHandler("Trainer not found with id: " + horseRequest.getTrainerId()));

        // create Horse Entity and add value for it
        Horse horse = mapper.map(horseRequest, Horse.class);
        horse.setTrainer(trainer);

        Horse horseAfterCreate = horseRepository.save(horse);

        // create horseResponse and return it into controller
        HorseResponse horseResponse = mapper.map(horseAfterCreate, HorseResponse.class);

        horseResponse.setTrainerId(horseAfterCreate.getTrainer().getId());
        horseResponse.setTrainerName(horseAfterCreate.getTrainer().getName());

        return horseResponse;
    }

    @Override
    @Transactional
    public HorseResponse updateHorse(Integer id, HorseRequest horseRequest) {

        Horse horse = horseRepository.findById(id).orElseThrow(() -> new GeneralExceptionHandler("Horse not found with id: " + id));

        horse.setFoaled(horseRequest.getFoaled() == null ? horse.getFoaled() : (Date) horseRequest.getFoaled());

        // validate horseName
        if (horseRequest.getName() != null) {
            if (horseRequest.getName().equals("")){
                throw new GeneralExceptionHandler("HorseName should not be blank");
            } else {
                horse.setName(horseRequest.getName());
            }
        }

        // check horseRequest.trainerId VS horse.trainerId;
        if (horseRequest.getTrainerId() != null && horseRequest.getTrainerId() != horse.getTrainer().getId()) {

            Trainer trainer = trainerRepository.findById(horseRequest.getTrainerId())
                    .orElseThrow(() -> new GeneralExceptionHandler("Trainer not found with id: " + horseRequest.getTrainerId()));

            horse.setTrainer(trainer);
        }

        // update horse into database
        Horse horseAfterUpdate = horseRepository.save(horse);

        HorseResponse horseResponse = mapper.map(horseAfterUpdate, HorseResponse.class);
        horseResponse.setTrainerId(horseAfterUpdate.getTrainer().getId());
        horseResponse.setTrainerName(horseAfterUpdate.getTrainer().getName());

        return horseResponse;
    }

    @Override
    @Transactional
    public void deleteHorse(Integer id) {

        horseRepository.findById(id).orElseThrow(() -> new GeneralExceptionHandler("Horse not found with id: " + id));

        horseRepository.deleteById(id);
    }
}
