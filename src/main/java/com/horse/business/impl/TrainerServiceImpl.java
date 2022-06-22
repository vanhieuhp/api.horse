package com.horse.business.impl;

import com.horse.business.general.FunctionUtil;
import com.horse.business.service.TrainerService;
import com.horse.config.exceptionHandler.GeneralExceptionHandler;
import com.horse.data.dto.trainer.TrainerRequest;
import com.horse.data.dto.trainer.TrainerResponse;
import com.horse.data.entity.Account;
import com.horse.data.entity.Horse;
import com.horse.data.entity.Trainer;
import com.horse.data.repository.AccountRepository;
import com.horse.data.repository.TrainerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
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

        Account account = accountRepository.findById(trainerRequest.getAccountId())
                .orElseThrow(() -> new GeneralExceptionHandler("Account not found with id: " + trainerRequest.getAccountId()));

        // convert request trainer into trainer
        Trainer trainer = new Trainer();
        trainer.setName(trainerRequest.getName());
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
    public TrainerResponse updateTrainer(Integer id, TrainerRequest trainerRequest) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new GeneralExceptionHandler("Trainer not found with id: " + id));

        trainer.setName(trainerRequest.getName() == null ? trainer.getName() : trainerRequest.getName());


        if (trainerRequest.getName() != null) {
            if (FunctionUtil.checkBlank(trainerRequest.getName()))
                throw new GeneralExceptionHandler("Name should not be blank");
            trainer.setName(trainerRequest.getName());
        }

        // set account for trainer
        if (trainerRequest.getAccountId() != null) {

            Account account = accountRepository.findById(trainerRequest.getAccountId())
                    .orElseThrow(() -> new GeneralExceptionHandler("Account not found with id: " + trainerRequest.getAccountId()));

            trainer.setAccount(account);
        }

        Trainer trainerAfterUpdate = trainerRepository.save(trainer);

        // get horse list for trainer
        List<String> horses = trainerAfterUpdate.getHorses().stream().map(Horse::getName).collect(Collectors.toList());

        // fill date into trainerResponse and return to controller
        TrainerResponse trainerResponse = mapper.map(trainerAfterUpdate, TrainerResponse.class);
        trainerResponse.setHorses(horses);
        trainerResponse.setAccountId(trainerAfterUpdate.getAccount().getId());
        trainerResponse.setAccountName(trainerAfterUpdate.getAccount().getUsername());

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
