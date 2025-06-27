package com.metodologia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.metodologia.controller.request.CreateUserDTO;
import com.metodologia.models.ERole;
import com.metodologia.models.RoleEntity;
import com.metodologia.models.UserEntity;
import com.metodologia.repositories.UserRepository;
import com.metodologia.repositories.RoleRepository;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        
        RoleEntity userRole = roleRepository.findByName(ERole.USER)
            .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        UserEntity userEntity = UserEntity.builder()
            .username(createUserDTO.getUsername())
            .password(passwordEncoder.encode(createUserDTO.getPassword()))
            .roles(roles)
            .nombre(createUserDTO.getNombre())
            .numero(createUserDTO.getNumero())
            .build();
        
        userRepository.save(userEntity);
        return ResponseEntity.ok("Usuario creado");
    }

    
    



    

    
}
