package org.marcos.autonomopro.model.db;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class ClienteDb {
    @Id
    @Column(name = "id_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El NIF/CIF es obligatorio")
    private String nifCif;

    @NotBlank(message = "El domicilio es obligatorio")
    private String domicilio;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Email(message = "El email debe ser válido")
    private String email;
 
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<AlbaranDb> albaranes;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<FacturaDb> facturas;
    
}
