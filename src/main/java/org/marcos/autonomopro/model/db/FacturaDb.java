package org.marcos.autonomopro.model.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "facturas")
public class FacturaDb {
    
    @Id
    @Column(name = "numero_factura", nullable = false)
    private String numeroFactura;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_emision")
    private Date fechaEmision;

    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    @Column(name = "forma_de_pago")
    private String formaDePago;

    @Column(name = "importe_total")
    private Long importeTotal;

    @Column(name = "importe_total_factura")
    private Long importeTotalFactura;

    @Column(name = "importe_total_iva")
    private Long importeTotalIVA;

    @Column(name = "importe_total_a_pagar")
    private Long importeTotalAPagar;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteDb cliente;

    /*@Column(name = "producto_codigo")
    private Long productoCodigo;*/

    @Column(name = "albaran_numero")
    private String albaranNumero;

    @OneToMany(mappedBy = "factura")
    private List<LineasDb> lineasFactura;

    /*@OneToOne
    @JoinColumn(name = "albaran_numero")
    private AlbaranDb albaran; Comentado porque tenia esto, pero para pasarle el albaran_numero a factura, que es string, no queria pasarle la entidad AlbaranDb*/
    
}
