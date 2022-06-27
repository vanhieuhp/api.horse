package com.horse.data.repository.role;

import com.horse.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

//    @Query("SELECT r FROM Role r WHERE r.code = ?1")
    Optional<Role> findRoleByCode(String code);
}