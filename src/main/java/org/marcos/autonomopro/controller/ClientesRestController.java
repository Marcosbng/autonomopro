package org.marcos.autonomopro.controller;

import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listar")
    public List<ClienteDb> listarClientes(@RequestParam(defaultValue = "nombre") String orderBy) {
        return clienteService.getListaClientes(orderBy);
    }

    @PostMapping("/crear")
    public ClienteDb crearCliente(@RequestBody ClienteDb cliente) {
        clienteService.crearCliente(cliente);
        return cliente;
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        ClienteDb cliente = clienteService.obtenerClientePorId(id);
        if (!cliente.getFacturas().isEmpty()) {
            return "No se puede eliminar el cliente porque está siendo utilizado en alguna factura";
        }
        clienteService.eliminarCliente(id);
        return "Cliente eliminado exitosamente";
    }

    @GetMapping("/perfil/{id}")
    public ClienteDb mostrarPerfilCliente(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @PutMapping("/modificar/{id}")
    public ClienteDb modificarCliente(@PathVariable Long id, @RequestBody ClienteDb cliente) {
        cliente.setId(id);
        return clienteService.modificarCliente(cliente);
    }
}
