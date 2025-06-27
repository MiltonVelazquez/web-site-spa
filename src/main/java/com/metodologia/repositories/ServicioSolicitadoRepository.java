package com.metodologia.repositories;

import com.metodologia.models.EState;
import com.metodologia.models.ServicioSolicitado;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioSolicitadoRepository extends CrudRepository<ServicioSolicitado, Long>{
    List<ServicioSolicitado> findByEstado(EState estado);
    List<ServicioSolicitado> findBySolicitanteIdAndEstado(Long solicitanteId, String estado);
    List<ServicioSolicitado> findByEstadoAndProfesionalId(EState estado, Long profesionalId);

}
