package com.horse.data.repository.account;

import com.horse.data.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>, AccountCustomRepository {

    @Query("select a from Account a where a.username = ?1")
    Optional<Account> findByUsername(String username);
}