package org.marcos.autonomopro.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.marcos.autonomopro.model.db.FacturaDb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaAnularFacturasVencidasTest {

    @Mock
    private FacturaService facturaService;

    @InjectMocks
    private TareaAnularFacturasVencidas tareaAnularFacturasVencidas;

    @BeforeEach
    void setUp() {
        // Configurar comportamiento simulado del servicio
        when(facturaService.obtenerFacturasVencidas(any())).thenReturn(new ArrayList<>());
    }

    @Test
    void testAnularFacturasVencidas() {
        // Llamar al método que queremos probar
        tareaAnularFacturasVencidas.anularFacturasVencidas();

        // Verificar que el método de servicio se llamó una vez
        verify(facturaService, times(1)).obtenerFacturasVencidas(any());

        // Verificar que no se llamó a modificarFactura() si no hay facturas vencidas
        verify(facturaService, never()).modificarFactura(any());

        // Simular facturas vencidas
        List<FacturaDb> facturasVencidas = new ArrayList<>();
        facturasVencidas.add(new FacturaDb());
        when(facturaService.obtenerFacturasVencidas(any())).thenReturn(facturasVencidas);

        // Llamar al método de nuevo
        tareaAnularFacturasVencidas.anularFacturasVencidas();

        // Verificar que modificarFactura() se llamó una vez para cada factura vencida
        verify(facturaService, times(facturasVencidas.size())).modificarFactura(any());
    }
}
