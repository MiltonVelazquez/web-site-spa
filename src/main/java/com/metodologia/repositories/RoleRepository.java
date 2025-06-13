package com.metodologia.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.metodologia.models.ERole;
import com.metodologia.models.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long>{
    Optional<RoleEntity> findByName(ERole name);
}
