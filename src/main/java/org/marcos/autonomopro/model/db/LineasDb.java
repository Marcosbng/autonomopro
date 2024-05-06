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
    @JoinColumn(name = "numero_factura")
    private FacturaDb factura;

    @ManyToOne
    @JoinColumn(name = "codigo_producto")
    private ProductoDb producto;

    @Column(name = "cantidad_producto")
    private int cantidadProducto;

    @Column(name = "descuento_aplicado")
    private Long descuentoAplicado;
    
}
