package com.metodologia.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTurnDTO {
    private String servicio;
    private String fecha;
    private String profesional;
    private String hora;
}
