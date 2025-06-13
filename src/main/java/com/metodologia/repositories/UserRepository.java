package com.metodologia.repositories;

import com.metodologia.models.ERole;
import com.metodologia.models.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByRoles_Name(ERole role);
}
