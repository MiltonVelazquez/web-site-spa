package com.metodologia.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.metodologia.models.ServicioSolicitado;
import com.metodologia.models.UserEntity;
import com.metodologia.repositories.UserRepository;
import com.metodologia.service.ServicioSolicitadoService;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServicioSolicitadoService servicioSolicitadoService;


    @DeleteMapping("/deleteUser/{userID}")
    public String deleteUser(@PathVariable("userID") Long userID){
        userRepository.deleteById(userID);
        return "Se ha borrado el user con id: " + (userID);
    }

    @GetMapping("/finalizados/{userId}")
    public ResponseEntity<List<ServicioSolicitado>> getTurnosFinalizadosCliente(@PathVariable Long userId) {
        List<ServicioSolicitado> turnos = servicioSolicitadoService.obtenerTurnosFinalizadosCliente(userId);
        if (turnos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/listarUser")
    public Iterable<UserEntity> getAll(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{userID}")
    public Optional<UserEntity> getByID(@PathVariable("userID") Long userID){
        return userRepository.findById(userID);
    }
}
