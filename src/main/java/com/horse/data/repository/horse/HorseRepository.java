package com.horse.data.repository.horse;

import com.horse.data.entity.Horse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorseRepository extends JpaRepository<Horse, Integer>, HorseCustomRepository {

}