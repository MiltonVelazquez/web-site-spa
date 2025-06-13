package com.metodologia.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.metodologia.controller.request.UpdateUserRoleDTO;
import com.metodologia.controller.request.UserManuallyDTO;
import com.metodologia.models.ERole;
import com.metodologia.models.RoleEntity;
import com.metodologia.models.ServicioSolicitado;
import com.metodologia.models.UserEntity;
import com.metodologia.repositories.RoleRepository;
import com.metodologia.repositories.UserRepository;
import com.metodologia.service.ServicioSolicitadoService;

import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServicioSolicitadoService servicioSolicitadoService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserManuallyDTO manuallyDTO){
    Set<RoleEntity> roles = new HashSet<>();

    for (String roleStr : manuallyDTO.getRoles()) {
        ERole roleEnum;
        try {
            roleEnum = ERole.valueOf(roleStr.toUpperCase()); // convierte a Enum
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: Rol inválido -> " + roleStr);
        }

        RoleEntity role = roleRepository.findByName(roleEnum)
            .orElseThrow(() -> new RuntimeException("Error: Rol " + roleEnum + " no encontrado"));

        roles.add(role);
    }

    UserEntity userEntity = UserEntity.builder()
        .username(manuallyDTO.getUsername())
        .password(passwordEncoder.encode(manuallyDTO.getPassword()))
        .roles(roles)
        .nombre(manuallyDTO.getNombre())
        .numero(manuallyDTO.getNumero())
        .build();

    userRepository.save(userEntity);
    return ResponseEntity.ok("Usuario creado con roles: " + manuallyDTO.getRoles());
    }

    @PutMapping("/user/{id}/roles")
    public ResponseEntity<?> updateUserRoles(
        @PathVariable Long id,
        @RequestBody UpdateUserRoleDTO dto) {

    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Set<RoleEntity> newRoles = dto.getRoles().stream()
        .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName))
            .orElseThrow(() -> new RuntimeException("Rol " + roleName + " no encontrado")))
        .collect(Collectors.toSet());

    user.setRoles(newRoles);
    userRepository.save(user);

    return ResponseEntity.ok("Roles actualizados correctamente");
    }

    @GetMapping("/usuarios")
    public List<UserEntity> getUsuariosConRolUser() {
        return userRepository.findByRoles_Name(ERole.USER);
    }

    @GetMapping("/profesionales")
    public List<UserEntity> getUsuariosConRolProfesional() {
        return userRepository.findByRoles_Name(ERole.PROFESSIONAL);
    }
}
