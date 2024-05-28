package org.marcos.autonomopro.controller;

import java.util.List;

import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.service.ClienteService;
import org.marcos.autonomopro.service.LineasService;
import org.marcos.autonomopro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private LineasService lineasService;

    @GetMapping("/listarProductos")
    public String listarProductos(Model model) {
        List<ProductoDb> listaProductos = productoService.getListaProductos();
        model.addAttribute("productos", listaProductos);
        return "listaProductos";
    }

    @GetMapping("/crearProducto")
    public String mostrarFormularioCreacionProducto(Model model) {
        model.addAttribute("producto", new ProductoDb());
        return "formularioProducto";
    }

    @PostMapping("/crearProducto")
    public String crearProducto(@ModelAttribute("producto") ProductoDb producto, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "formularioProducto";
        }

        productoService.crearProducto(producto);
        attributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
        return "redirect:/listarProductos";
    }

    @GetMapping("/eliminar/producto/{codigo}")
    public String eliminarProducto(@PathVariable Long codigo, RedirectAttributes attributes) {
        // Verificar si el producto está siendo utilizado en alguna factura
        boolean enUso = lineasService.existeProductoEnFactura(codigo);

        if (enUso) {
            // Si el producto está en uso, mostrar un mensaje y redirigir de vuelta a la
            // lista de productos
            attributes.addFlashAttribute("mensajeError",
                    "No se puede eliminar el producto que está en proceso de facturación");
        } else {
            // Si el producto no está en uso, proceder con la eliminación
            productoService.eliminarProductoPorCodigo(codigo);
            attributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        }

        return "redirect:/listarProductos";
    }

    @GetMapping("/editar/producto/{codigo}")
    public String mostrarFormularioEdicionProducto(@PathVariable Long codigo, Model model) {
        ProductoDb producto = productoService.obtenerProductoPorCodigo(codigo);
        model.addAttribute("producto", producto);
        return "formModificarProducto";
    }

    @PostMapping("/editar/producto/{codigo}")
    public String editarProducto(@ModelAttribute("producto") ProductoDb producto, RedirectAttributes attributes) {
        productoService.actualizarProducto(producto);
        attributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        return "redirect:/listarProductos";
    }

}
