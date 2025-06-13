package com.metodologia.controller.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserManuallyDTO {
    private String username;
    private String password;
    private String nombre;
    private Set<String> roles;
    private int numero;

}
