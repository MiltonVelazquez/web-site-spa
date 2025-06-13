package com.metodologia.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombre", unique = true)
    private String nombre;

    //@ManyToMany(mappedBy = "servicios")
   // private List<ServicioSolicitado> solicitudes;

    @Column(name = "precio")
    private int precio;

    @Column(name = "tiempo")
    private int tiempo;
}