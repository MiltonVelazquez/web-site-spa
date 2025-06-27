package com.metodologia.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceDTO {
    private String nombre;
    private int precio;
    private int tiempo;
    private String descripcion;
}
