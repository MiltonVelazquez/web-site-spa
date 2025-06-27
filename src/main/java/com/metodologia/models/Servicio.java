package com.metodologia.models;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private int precio;

    @Column(name = "tiempo", nullable = false)
    private int tiempo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    private List<ServicioSolicitadoDetalle> detalles; // opcional
}
