package com.metodologia.controller.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTurnDTO {
    private Long solicitanteId;
    private Long profesionalId;
    private List<Long> servicioIds;
    private String fecha;
    private String hora;
    private String estado; // "PENDIENTE", "FINALIZADO", etc.
}
