package com.metodologia.controller.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnDetail {
    private Long id;
    private String fecha;
    private String hora;
    private String estado;

    private CreateUserDTO solicitante;
    private CreateUserDTO profesional;
    private List<CreateServiceDTO> servicios;
}
