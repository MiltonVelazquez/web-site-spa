package com.metodologia.repositories;

import com.metodologia.models.ServicioSolicitado;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioSolicitadoRepository extends CrudRepository<ServicioSolicitado, Long>{
    
}
