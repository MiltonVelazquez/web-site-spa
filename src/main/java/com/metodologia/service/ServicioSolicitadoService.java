package com.metodologia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metodologia.models.ServicioSolicitado;
import com.metodologia.repositories.ServicioSolicitadoRepository;

@Service
public class ServicioSolicitadoService {
    @Autowired
    private ServicioSolicitadoRepository servicioSolicitadoRepository;

    public List<ServicioSolicitado> obtenerTurnosFinalizadosCliente(Long userId) {
        return servicioSolicitadoRepository.findBySolicitanteIdAndEstado(userId, "FINALIZADO");
    }
}
