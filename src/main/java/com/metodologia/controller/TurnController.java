package com.metodologia.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metodologia.controller.request.CreateTurnDTO;
import com.metodologia.models.ServicioSolicitado;
import com.metodologia.repositories.ServicioSolicitadoRepository;

import jakarta.validation.Valid;

@RestController
public class TurnController {

    @Autowired
    private ServicioSolicitadoRepository servicioSolicitado;


    @PostMapping("/solicitarTurno")
    public ResponseEntity<?> createTurn(@Valid @RequestBody CreateTurnDTO createTurnDTO){
        ServicioSolicitado servicio = ServicioSolicitado.builder()
            .servicio(createTurnDTO.getServicio())
            .fecha(createTurnDTO.getFecha())
            .profesional(createTurnDTO.getProfesional())
            .hora(createTurnDTO.getHora())
            .build();

        servicioSolicitado.save(servicio);
        return ResponseEntity.ok(servicio);
    }

    @DeleteMapping("/deleteTurn/{turnID}")
    public String deleteTurn(@PathVariable("turnID") Long turnID){
        servicioSolicitado.deleteById(turnID);
        return "Se ha borrado el turno con id: " + turnID;
    }

    @GetMapping("/listTurn")
    public Iterable<ServicioSolicitado> getAll(){
        return servicioSolicitado.findAll();
    }

    @GetMapping("/turn/{turnID}")
    public Optional<ServicioSolicitado> getByID(@PathVariable("turnID") Long turnID){
        return servicioSolicitado.findById(turnID);
    }

}
