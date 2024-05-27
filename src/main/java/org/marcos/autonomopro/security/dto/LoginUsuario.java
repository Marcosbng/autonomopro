package org.marcos.autonomopro.security.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginUsuario {
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
}
