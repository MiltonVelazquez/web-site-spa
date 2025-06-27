package com.metodologia.repositories;

import com.metodologia.models.Servicio;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends CrudRepository<Servicio, Long>{

}
