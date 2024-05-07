package org.marcos.autonomopro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.repository.ClientesRepository;

public class ClienteServiceTest {

    private ClientesRepository clientesRepository;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clientesRepository = mock(ClientesRepository.class);
        clienteService = new ClienteService(clientesRepository);
    }

    @Test
    void testCrearCliente() {
        ClienteDb cliente = new ClienteDb();
        clienteService.crearCliente(cliente);
        verify(clientesRepository).save(cliente);
    }

    @Test
    void testEliminarCliente() {
        Long id = 1L;
        clienteService.eliminarCliente(id);
        verify(clientesRepository).deleteById(id);
    }

    @Test
    void testGetListaClientes() {
        List<ClienteDb> clientes = new ArrayList<>();
        when(clientesRepository.findAll()).thenReturn(clientes);
        List<ClienteDb> result = clienteService.getListaClientes();
        assertEquals(clientes, result);
    }

    @Test
    void testModificarCliente() {
        ClienteDb cliente = new ClienteDb();
        clienteService.modificarCliente(cliente);
        verify(clientesRepository).save(cliente);
    }

    @Test
    void testMostrarFormularioCreacion() {
        ClienteDb cliente = clienteService.mostrarFormularioCreacion();
        assertEquals(new ClienteDb(), cliente);
    }

    @Test
    void testMostrarFormularioEliminacion() {
        Long id = 1L;
        when(clientesRepository.findById(id)).thenReturn(Optional.of(new ClienteDb()));
        ClienteDb cliente = clienteService.mostrarFormularioEliminacion(id);
        assertEquals(new ClienteDb(), cliente);
    }

    @Test
    void testMostrarFormularioModificacion() {
        Long id = 1L;
        when(clientesRepository.findById(id)).thenReturn(Optional.of(new ClienteDb()));
        ClienteDb cliente = clienteService.mostrarFormularioModificacion(id);
        assertEquals(new ClienteDb(), cliente);
    }

    @Test
    void testObtenerClientePorId() {
        Long id = 1L;
        when(clientesRepository.findById(id)).thenReturn(Optional.of(new ClienteDb()));
        ClienteDb cliente = clienteService.obtenerClientePorId(id);
        assertEquals(new ClienteDb(), cliente);
    }
}
