package com.metodologia.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    // Usuario que solicita el servicio
    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private UserEntity solicitante;
    
    @OneToMany(mappedBy = "servicioSolicitado", cascade = CascadeType.ALL)
    private List<ServicioSolicitadoDetalle> detalles;
    
    @Column(name = "fecha")
    private String fecha;

    // Usuario que brinda el servicio (profesional)
    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private UserEntity profesional;
     
    @Column(name = "hora")
    private String hora;

    // Enum de estado
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum ("EN_PROCESO", etc.)
    private EState estado;

}