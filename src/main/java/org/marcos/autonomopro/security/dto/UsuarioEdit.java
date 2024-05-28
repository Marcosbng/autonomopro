package org.marcos.autonomopro.security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioEdit implements Serializable {

    @NotNull
    private String nombre;
    @NotNull
    private String nickname;
    @NotNull
    private String email;
}
