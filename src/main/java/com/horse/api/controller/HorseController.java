package com.horse.api.controller;

import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;
import com.horse.business.service.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/horses")
public class HorseController {

    @Autowired
    private HorseService horseService;

    @PostMapping
    public ResponseEntity<HorseResponse> createHorse(@RequestBody @Valid HorseRequest horseRequest) {
        HorseResponse horseResponse = horseService.createHorse(horseRequest);
        return new ResponseEntity<>(horseResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorseResponse> updateHorse(@PathVariable("id") Integer id ,@RequestBody @Valid HorseRequest horseRequest) {
        HorseResponse horseResponse = horseService.updateHorse(id, horseRequest);
        return new ResponseEntity<>(horseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable("id") Integer id) {
        horseService.deleteHorse(id);
    }

    @GetMapping("/filter")
    private ResponseEntity<List<HorseResponse>>  getByFilter(HttpServletRequest request) {

        Integer trainerId = null;
        Integer year = null;

        if (request.getParameter("trainer_id") != null) {
            trainerId = Integer.valueOf(request.getParameter("trainer_id"));
        }

        if (request.getParameter("year") != null) {
            year = Integer.valueOf(request.getParameter("year"));
        }

        List<HorseResponse> horsesResponse = horseService.findByTrainerAndDate(trainerId, year);
        return new ResponseEntity<>(horsesResponse, HttpStatus.OK);

    }

    @GetMapping()
    private ResponseEntity<List<HorseResponse>>  findAll() {

        List<HorseResponse> horsesResponse = horseService.findAll();
        return new ResponseEntity<>(horsesResponse, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    private ResponseEntity<HorseResponse> getOne(@PathVariable("id") Integer id) {

        HorseResponse horseResponse = horseService.getOne(id);
        return new ResponseEntity<>(horseResponse, HttpStatus.OK);
    }
}
