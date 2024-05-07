package org.marcos.autonomopro.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.marcos.autonomopro.model.db.LineasDb;
import org.marcos.autonomopro.repository.LineasRepository;

@ExtendWith(MockitoExtension.class)
public class LineasServiceTest {

    @Mock
    private LineasRepository lineasRepository;

    @InjectMocks
    private LineasService lineasService;

    @Test
    void testCrearLinea() {
        LineasDb linea = new LineasDb();

        lineasService.crearLinea(linea);
        verify(lineasRepository, times(1)).save(linea);
    }

    @Test
    void testEliminarLineasPorNumeroFactura() {
        String numeroFactura = "12345";
        List<LineasDb> lineas = new ArrayList<>();
        lineas.add(new LineasDb());
        lineas.add(new LineasDb());

        when(lineasRepository.findByFacturaNumeroFactura(numeroFactura)).thenReturn(lineas);

        lineasService.eliminarLineasPorNumeroFactura(numeroFactura);
        verify(lineasRepository, times(2)).delete(lineas.get(0));
        verify(lineasRepository, times(2)).delete(lineas.get(1));
    }
}
