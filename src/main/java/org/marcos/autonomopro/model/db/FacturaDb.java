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
    private float importeTotal;

    @Column(name = "importe_total_iva")
    private float importeTotalIVA;

    @Column(name = "importe_total_a_pagar")
    private float importeTotalAPagar;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteDb cliente;

    @Column(name = "albaran_numero")
    private String albaranNumero;

    @OneToMany(mappedBy = "factura")
    private List<LineasDb> lineasFactura;
    
}
