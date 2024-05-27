package org.marcos.autonomopro.security.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NuevoUsuario {
    @NotBlank
    private String nickname;
    @NotBlank
    private String nombre;
    @Email
    private String email;
    @NotBlank
    private String password;
    //Al utilizar API Rest utilizamos objetos tipo Json y 
    //es mejor que sean cadenas para agilizar el tráfico
    private Set<String> roles = new HashSet<>();
}
