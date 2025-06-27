package com.metodologia.models;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicio_solicitado")
public class ServicioSolicitado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private UserEntity solicitante;

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    private UserEntity profesional;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "hora", nullable = false)
    private String hora;

    @Enumerated(EnumType.STRING)
    private EState estado;

    @OneToMany(mappedBy = "servicioSolicitado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicioSolicitadoDetalle> detalles;
}
