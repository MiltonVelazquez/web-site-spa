package com.metodologia.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servicio_solicitado_detalle")
public class ServicioSolicitadoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    @JsonIgnore
    private Servicio servicio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servicio_solicitado_id", nullable = false)
    @JsonIgnore
    private ServicioSolicitado servicioSolicitado;

    // Podés agregar campos extra si querés, como:
    // private int precioFinal;
}
