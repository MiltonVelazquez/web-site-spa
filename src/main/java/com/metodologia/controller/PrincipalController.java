package com.metodologia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.metodologia.controller.request.CreateTurnDTO;
import com.metodologia.controller.request.CreateUserDTO;
import com.metodologia.models.ERole;
import com.metodologia.models.RoleEntity;
import com.metodologia.models.UserEntity;
import com.metodologia.models.ServicioSolicitado;
import com.metodologia.repositories.UserRepository;
import com.metodologia.repositories.ServicioSolicitadoRepository;

import jakarta.validation.Valid;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServicioSolicitadoRepository servicioSolicitadoRepository;

    @GetMapping("/hello")
    public String hello(){
        return "Hoooola";
    }

    @GetMapping("/helloSeguro")
    public String helloSeguro(){
        return "hola no";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        
        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
            .map(role -> RoleEntity.builder()
                .name(ERole.valueOf(role))
                .build())
            .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
            .username(createUserDTO.getUsername())
            .password(passwordEncoder.encode(createUserDTO.getPassword()))
            .roles(roles)
            .build();
        
        userRepository.save(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    
    @PostMapping("/solicitarTurno")
    public ResponseEntity<?> createTurn(@Valid @RequestBody CreateTurnDTO createTurnDTO){
        ServicioSolicitado servicioSolicitado = ServicioSolicitado.builder()
            .servicio(createTurnDTO.getServicio())
            .fecha(createTurnDTO.getFecha())
            .profesional(createTurnDTO.getProfesional())
            .hora(createTurnDTO.getHora())
            .build();

        servicioSolicitadoRepository.save(servicioSolicitado);
        return ResponseEntity.ok(servicioSolicitado);
    }



    

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id: ".concat(id);
    }
}
