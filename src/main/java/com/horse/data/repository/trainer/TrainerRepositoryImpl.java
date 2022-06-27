package com.horse.data.repository.trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TrainerRepositoryImpl implements TrainerCustomRepository{

    @Autowired
    private EntityManager entityManager;
}
