package org.marcos.autonomopro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.repository.FacturasRepository;
import org.marcos.autonomopro.repository.LineasRepository;
import org.marcos.autonomopro.repository.ProductosRepository;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTest {

    @Mock
    private LineasRepository lineasRepository;
    
    @Mock
    private FacturasRepository facturaRepository;

    @Mock
    private ProductosRepository productosRepository;

    @InjectMocks
    private FacturaService facturaService;

    // Simulamos el ProductoService
    private ProductoService productoService = new ProductoService();

    @Test
    void testCrearFactura() {
        FacturaDb factura = new FacturaDb();
        factura.setNumeroFactura("12345");
        factura.setEstado("Pendiente");
        factura.setFechaEmision(new Date(System.currentTimeMillis()));
        factura.setFechaVencimiento(new Date(System.currentTimeMillis()));
        factura.setFormaDePago("Transferencia bancaria");
        factura.setImporteTotal(100.0f);
        factura.setImporteTotalIVA(10.0f);
        factura.setImporteTotalAPagar(110.0f);

        facturaService.crearFactura(factura);
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void testModificarFactura() {
        FacturaDb factura = new FacturaDb();
        factura.setNumeroFactura("12345");

        when(facturaRepository.save(factura)).thenReturn(factura);

        facturaService.modificarFactura(factura);
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void testGenerarNumeroFactura() {
        String numeroFacturaGenerado = facturaService.generarNumeroFactura();
        assertNotNull(numeroFacturaGenerado);
        // Implementa más pruebas aquí para verificar si el número de factura generado sigue el formato deseado y es único.
    }

    @Test
    void testObtenerFacturaPorNumero_Existente() {
        String numeroFactura = "12345";
        FacturaDb factura = new FacturaDb();
        factura.setNumeroFactura(numeroFactura);

        when(facturaRepository.findByNumeroFactura(numeroFactura)).thenReturn(Optional.of(factura));

        FacturaDb facturaObtenida = facturaService.obtenerFacturaPorNumero(numeroFactura);
        assertNotNull(facturaObtenida);
        assertEquals(numeroFactura, facturaObtenida.getNumeroFactura());
    }

    @Test
    void testObtenerFacturaPorNumero_NoExistente() {
        String numeroFactura = "12345";

        when(facturaRepository.findByNumeroFactura(numeroFactura)).thenReturn(Optional.empty());

        FacturaDb facturaObtenida = facturaService.obtenerFacturaPorNumero(numeroFactura);
        assertNull(facturaObtenida);
    }

    @Test
    void testEliminarFacturaPorNumeroFactura() {
        String numeroFactura = "12345";

        FacturaDb factura = new FacturaDb();
        factura.setNumeroFactura(numeroFactura);

        when(facturaRepository.findByNumeroFactura(numeroFactura)).thenReturn(Optional.of(factura));

        facturaService.eliminarFacturaPorNumeroFactura(numeroFactura);
        verify(facturaRepository, times(1)).delete(factura);
    }

    @Test
    void testObtenerFacturasCliente() {
        Long idCliente = 1L;

        List<FacturaDb> facturas = new ArrayList<>();
        facturas.add(new FacturaDb());
        facturas.add(new FacturaDb());

        when(facturaRepository.findByClienteId(idCliente)).thenReturn(facturas);

        List<FacturaDb> listaFacturas = facturaService.obtenerFacturasCliente(idCliente);
        assertEquals(2, listaFacturas.size());
    }
}
