package org.marcos.autonomopro.service;

import java.util.List;
import java.util.Optional;

import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClientesRepository clientesRepository;

    @Autowired
    public ClienteService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    // método obtener lista de clientes
    public List<ClienteDb> getListaClientes() {
        return clientesRepository.findAll();
    }

    // método mostrar formulario de creación de cliente
    public ClienteDb mostrarFormularioCreacion() {
        return new ClienteDb();
    }

    // método crear un cliente
    public void crearCliente(ClienteDb cliente) {
        clientesRepository.save(cliente);
    }

    // método mostrar formulario de eliminación de cliente
    public ClienteDb mostrarFormularioEliminacion(Long id) {
        return clientesRepository.findById(id).orElse(null);
    }

    // método eliminar un cliente
    public void eliminarCliente(Long id) {
        clientesRepository.deleteById(id);
    }

    // método obtener un cliente por su ID
    public ClienteDb obtenerClientePorId(Long id) {
        Optional<ClienteDb> clienteOptional = clientesRepository.findById(id);
        return clienteOptional.orElse(null);
    }

    public ClienteDb mostrarFormularioModificacion(Long id) {
        return clientesRepository.findById(id).orElse(null);
    }

    public void modificarCliente(ClienteDb cliente) {
        clientesRepository.save(cliente);
    }
    
}
