package org.marcos.autonomopro.controller;

import java.util.List;

import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.service.ClienteService;
import org.marcos.autonomopro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/listarProductos")
    public String listarProductos(Model model) {
        List<ProductoDb> listaProductos = productoService.getListaProductos();
        model.addAttribute("productos", listaProductos);
        return "listaProductos"; // Vista que mostrará la lista de productos
    }

    @GetMapping("/crearProducto")
    public String mostrarFormularioCreacionProducto(Model model) {
        model.addAttribute("producto", new ProductoDb());
        return "formularioProducto"; // Vista que contiene el formulario de creación de producto
    }

    @PostMapping("/crearProducto")
    public String crearProducto(@ModelAttribute("producto") ProductoDb producto, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "formularioProducto"; // Volver a mostrar el formulario con los errores
        }

        productoService.crearProducto(producto);
        attributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
        return "redirect:/listarProductos"; // Redirigir a la lista de productos después de la creación
    }

}
