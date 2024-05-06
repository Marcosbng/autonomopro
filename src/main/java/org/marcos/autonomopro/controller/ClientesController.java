package org.marcos.autonomopro.controller;

import java.util.List;

import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.repository.ClientesRepository;
import org.marcos.autonomopro.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientesController {

    @Autowired
    private ClienteService clientesService;

    @GetMapping("/listarClientes")
    public String listarClientes(Model model) {
        List<ClienteDb> listaClientes = clientesService.getListaClientes();
        model.addAttribute("clientes", listaClientes);
        return "listaClientes"; // vista que mostrará la lista de clientes
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("cliente", clientesService.mostrarFormularioCreacion());
        return "formularioCliente"; // vista que contiene el formulario de creación de cliente
    }

    @PostMapping("/crear")
    public String crearCliente(@ModelAttribute("cliente") ClienteDb cliente, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "formularioCliente"; // volver a mostrar el formulario con los errores
        }

        clientesService.crearCliente(cliente);
        attributes.addFlashAttribute("mensaje", "Cliente creado exitosamente");
        return "redirect:/listarClientes"; // redirigir a la lista de clientes después de la creación
    }

    @GetMapping("/eliminar/cliente/{id}")
    public String mostrarFormularioEliminacion(@PathVariable("id") Long id, Model model) {
        ClienteDb cliente = clientesService.mostrarFormularioEliminacion(id);
        model.addAttribute("cliente", cliente);
        return "formEliminarCliente"; // vista para confirmar la eliminación del cliente
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id, RedirectAttributes attributes) {
        ClienteDb cliente = clientesService.mostrarFormularioEliminacion(id);
        if (cliente == null) {
            attributes.addFlashAttribute("mensajeError", "El cliente no se encontró en la base de datos");
            return "redirect:/listarClientes"; // redirige a la lista de clientes
        }
        clientesService.eliminarCliente(id);
        attributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        return "redirect:/listarClientes"; // redirige a la lista de clientes después de la eliminación
    }

    @GetMapping("/perfil/cliente/{id}")
    public String mostrarPerfilCliente(@PathVariable("id") Long id, Model model) {
        ClienteDb cliente = clientesService.obtenerClientePorId(id);
        model.addAttribute("cliente", cliente);
        return "perfilCliente"; // vista para mostrar el perfil del cliente
    }

    @GetMapping("/modificar/cliente/{id}")
    public String mostrarFormularioModificacion(@PathVariable("id") Long id, Model model) {
        ClienteDb cliente = clientesService.mostrarFormularioModificacion(id);
        model.addAttribute("cliente", cliente);
        return "formModificarCliente"; // vista para modificar los datos del cliente
    }

    @PostMapping("/modificar/cliente/{id}")
    public String modificarCliente(@PathVariable("id") Long id, @ModelAttribute("cliente") ClienteDb cliente,
            BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "formModificarCliente"; // volver a mostrar el formulario con los errores
        }

        clientesService.modificarCliente(cliente);
        attributes.addFlashAttribute("mensaje", "Cliente modificado exitosamente");
        return "redirect:/perfil/cliente/{id}"; // redirige al perfil después de la modificación
    }

}
