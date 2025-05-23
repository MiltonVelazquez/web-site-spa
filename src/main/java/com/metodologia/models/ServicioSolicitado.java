package com.metodologia.models;

import java.util.Date;

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
    
    //@ManyToOne
    //@JoinColumn(name = "persona_id")
    //private UserEntity persona;
    
    //@ManyToOne
    //@JoinColumn(name = "servicio_id")
    private String servicio;
    
    @Column(name = "fecha")
    private String fecha;

    @Column(name = "profesional")
    private String profesional;
     
    @Column(name = "hora")
    private String hora;


}