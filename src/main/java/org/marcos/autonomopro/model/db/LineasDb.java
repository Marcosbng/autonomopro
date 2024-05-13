package org.marcos.autonomopro.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lineas")
public class LineasDb {

    @Id
    @Column(name = "id_linea")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLinea;

    @ManyToOne
    @JoinColumn(name = "num_factura")
    private FacturaDb factura;

    @ManyToOne
    @JoinColumn(name = "cod_prod")
    private ProductoDb producto;

    @Column(name = "cantidad_prod")
    private int cantidadProducto;

    @Column(name = "desc_aplicado")
    private Long descuentoAplicado;
    
}
