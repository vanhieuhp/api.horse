package com.horse.data.repository;

import com.horse.data.entity.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HorseRepository extends JpaRepository<Horse, Integer>, HorseCustomRepository {

    @Query("DELETE FROM Horse h where h.id = ?1")
    void deleteHorseById(Integer id);
}