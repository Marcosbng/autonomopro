package org.marcos.autonomopro.model.db;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class ProductoDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_prod")
    private Long codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio_uni")
    private float precioUnitario;

    @Column(name = "iva")
    private float iva;

    @OneToMany(mappedBy = "producto")
    private List<LineasDb> lineasFactura;

}
