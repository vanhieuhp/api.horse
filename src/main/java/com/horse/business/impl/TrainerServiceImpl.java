package com.horse.business.impl;

import com.horse.business.general.FunctionUtil;
import com.horse.business.service.TrainerService;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;
import com.horse.data.entity.Account;
import com.horse.data.entity.Horse;
import com.horse.data.entity.Trainer;
import com.horse.data.repository.account.AccountRepository;
import com.horse.data.repository.trainer.TrainerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public TrainerResponse createTrainer(TrainerRequest trainerRequest) {

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // convert request trainer into trainer
        Trainer trainer = mapper.map(trainerRequest, Trainer.class);
        trainer.setAccount(account);

        Trainer trainerAfterCreate = trainerRepository.save(trainer);

        // return trainerResponse to controller
        TrainerResponse trainerResponse = mapper.map(trainerAfterCreate, TrainerResponse.class);
        trainerResponse.setAccountId(account.getId());
        trainerResponse.setAccountName(account.getUsername());

        return trainerResponse;
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(Integer trainerId, TrainerRequest trainerRequest) {

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer trainer = account.getTrainers().stream().filter(trainers -> trainers.getId() == trainerId).findFirst().orElse(null);
        if (trainer == null) {
            throw new GeneralExceptionHandler("TrainerId not found with account: " + account.getUsername());
        }

        // validate field in trainer
        if (trainerRequest.getName() != null) {
            FunctionUtil.checkBlank(trainerRequest.getName(), "name");
            trainer.setName(trainerRequest.getName());
        }

        if (trainerRequest.getAge() != null) {
            FunctionUtil.checkAgeTrainer(trainerRequest.getAge());
        }

        if (trainerRequest.getAddress() != null) {
            FunctionUtil.checkBlank(trainerRequest.getAddress(), "address");
            trainer.setAddress(trainerRequest.getAddress());
        }

        if (trainerRequest.getGender() != null) {
            FunctionUtil.checkBlank(trainerRequest.getGender(), "gender");
            trainer.setAddress(trainerRequest.getGender());
        }

        //update trainer
        Trainer trainerAfterUpdate = trainerRepository.save(trainer);

        // get horse list for trainer
        List<String> horses = trainerAfterUpdate.getHorses().stream().map(Horse::getName).collect(Collectors.toList());

        // fill date into trainerResponse and return to controller
        TrainerResponse trainerResponse = mapper.map(trainerAfterUpdate, TrainerResponse.class);
        trainerResponse.setHorses(horses);
        trainerResponse.setAccountId(account.getId());
        trainerResponse.setAccountName(account.getUsername());

        return trainerResponse;
    }

    @Override
    @Transactional
    public void deleteTrainer(Integer id) {

        Trainer trainer =  trainerRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Trainer not found with id: " + id));

        // check trainer if trainerID is fk of horse
        if (!trainer.getHorses().isEmpty()) {
            throw new RuntimeException("Trainer is foreign key in the horse table! Can't delete!");
        }

        trainerRepository.delete(trainer);
    }

    @Override
    public TrainerResponse getOne(Integer id) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Trainer not found with id: " + id));


        // get list of horses
        List<String> horses = trainer.getHorses().stream().map(Horse::getName).collect(Collectors.toList());

        // fill data into trainer response
        TrainerResponse trainerResponse = mapper.map(trainer, TrainerResponse.class);
        trainerResponse.setHorses(horses);
        trainerResponse.setAccountId(trainer.getAccount().getId());
        trainerResponse.setAccountName(trainer.getAccount().getUsername());

        return trainerResponse;
    }


}
