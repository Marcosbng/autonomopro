package org.marcos.autonomopro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.repository.AlbaranRepository;

@ExtendWith(MockitoExtension.class)
public class AlbaranServiceTest {

    @Mock
    private AlbaranRepository albaranRepository;

    @InjectMocks
    private AlbaranService albaranService;

    @Test
    void testCrearAlbaran() {
        AlbaranDb albaran = new AlbaranDb();
        albaran.setNumeroAlbaran("12345");

        albaranService.crearAlbaran(albaran);
        verify(albaranRepository, times(1)).save(albaran);
    }

    @Test
    void testEliminarAlbaranPorNumeroAlbaran() {
        String numeroAlbaran = "12345";
        AlbaranDb albaran = new AlbaranDb();
        albaran.setNumeroAlbaran(numeroAlbaran);

        when(albaranRepository.findByNumeroAlbaran(numeroAlbaran)).thenReturn(albaran);

        albaranService.eliminarAlbaranPorNumeroAlbaran(numeroAlbaran);
        verify(albaranRepository, times(1)).delete(albaran);
    }

    @Test
    void testGenerarNumeroAlbaran() {
        String numeroAlbaran = albaranService.generarNumeroAlbaran();
        assertNotNull(numeroAlbaran);
    }

    @Test
    void testGetListaAlbaranes() {
        List<AlbaranDb> albaranes = new ArrayList<>();
        albaranes.add(new AlbaranDb());
        albaranes.add(new AlbaranDb());

        when(albaranRepository.findAll()).thenReturn(albaranes);

        List<AlbaranDb> listaAlbaranes = albaranService.getListaAlbaranes();
        assertEquals(2, listaAlbaranes.size());
    }

    @Test
    void testObtenerAlbaranPorNumero() {
        String numeroAlbaran = "12345";
        AlbaranDb albaran = new AlbaranDb();
        albaran.setNumeroAlbaran(numeroAlbaran);

        when(albaranRepository.findByNumeroAlbaran(numeroAlbaran)).thenReturn(albaran);

        AlbaranDb albaranObtenido = albaranService.obtenerAlbaranPorNumero(numeroAlbaran);
        assertNotNull(albaranObtenido);
        assertEquals(numeroAlbaran, albaranObtenido.getNumeroAlbaran());
    }
}
