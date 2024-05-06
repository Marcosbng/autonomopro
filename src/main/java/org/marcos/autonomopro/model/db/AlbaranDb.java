package org.marcos.autonomopro.model.db;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "albaranes")
public class AlbaranDb {

    @Id
    @Column(name = "numero_albaran")
    private String numeroAlbaran;

    private Date fecha;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Column(name = "codigo_producto")
    private Long codigoProducto;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteDb cliente;

    /*@OneToMany(mappedBy = "albaran")
    private List<FacturaDb> facturas;*/

}
