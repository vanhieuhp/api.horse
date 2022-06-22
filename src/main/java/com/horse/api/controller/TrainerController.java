package com.horse.api.controller;

import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;
import com.horse.business.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<TrainerResponse> createTrainer(@RequestBody @Valid TrainerRequest trainerRequest) {

        TrainerResponse trainerResponse = trainerService.createTrainer(trainerRequest);

        return new ResponseEntity<>(trainerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainerResponse> updateTrainer(@PathVariable("id") Integer id, @RequestBody TrainerRequest trainerRequest) {

        TrainerResponse trainerResponse = trainerService.updateTrainer(id , trainerRequest);

        return new ResponseEntity<>(trainerResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable("id") Integer id) {
        trainerService.deleteTrainer(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getOne(@PathVariable("id") Integer id) {

        TrainerResponse trainerResponse = trainerService.getOne(id);
        return new ResponseEntity<>(trainerResponse, HttpStatus.BAD_REQUEST);

    }
}
