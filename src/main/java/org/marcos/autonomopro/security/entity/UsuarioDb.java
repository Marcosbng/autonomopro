package org.marcos.autonomopro.security.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public class UsuarioDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    @Column(unique = true)
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    private String password;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    // En la tabla 'usuarios_roles' queremos sacar todos los 'idRol' correspondientes
    // al 'idUsuario' actual para poder generar la lista de 'roles' en 'UsuarioDb'.
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "idUsuario"), inverseJoinColumns = @JoinColumn(name = "idRol"))
    private Set<RolDb> roles = new HashSet<>();
    // Constructor con todos los campos menos 'id'.
    public UsuarioDb(@NotNull String nombre, @NotNull String nickname, @NotNull String email, @NotNull String password) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
