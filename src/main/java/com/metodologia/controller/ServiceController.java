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

import com.metodologia.controller.request.CreateServiceDTO;
import com.metodologia.models.Servicio;
import com.metodologia.repositories.ServicioRepository;

import jakarta.validation.Valid;

@RestController
public class ServiceController {

    @Autowired
    private ServicioRepository servicioRepo;

    @PostMapping("/crearServicio")
    public ResponseEntity<?> createService(@Valid @RequestBody CreateServiceDTO createServiceDTO){
        Servicio servicio = Servicio.builder()
            .nombre(createServiceDTO.getNombre())
            .precio(createServiceDTO.getPrecio())
            .tiempo(createServiceDTO.getTiempo())
            .descripcion(createServiceDTO.getDescripcion())
        .build();
        
        servicioRepo.save(servicio);
        return ResponseEntity.ok(servicio);
    }

    @DeleteMapping("/deleteServicio/{serviceID}")
    public String deleteService(@PathVariable("serviceID") Long serviceID){
        servicioRepo.deleteById(serviceID);
        return "Se ha borrado el servicio con id: " + serviceID;
    }

    @GetMapping("/listaServicio")
    public Iterable<Servicio> getAll(){
        return servicioRepo.findAll();
    }

    @GetMapping("/servicio/{servicioID}")
    public Optional<Servicio> getByID(@PathVariable("servicioID") Long servicioID){
        return servicioRepo.findById(servicioID);
    }

}
