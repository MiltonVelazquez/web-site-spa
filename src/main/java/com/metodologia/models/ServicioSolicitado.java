package com.metodologia.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicio_solicitado")
public class ServicioSolicitado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "persona_id")
    private UserEntity persona;
    
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;
    
    @Column(name = "hora_fecha")
    private String horaFecha;
}