package org.marcos.autonomopro.controller;

import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/productos")
public class ProductosRestController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public List<ProductoDb> listarProductos() {
        return productoService.getListaProductos();
    }

    @PostMapping("/crear")
    public String crearProducto(@RequestBody ProductoDb producto) {
        productoService.crearProducto(producto);
        return "Producto creado exitosamente";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminar/{codigo}")
    public String eliminarProducto(@PathVariable Long codigo) {
        boolean eliminado = productoService.eliminarProductoPorCodigo(codigo);
        if (eliminado) {
            return "Producto eliminado exitosamente";
        } else {
            return "No se puede eliminar el producto que está en proceso de facturación";
        }
    }

    @PutMapping("/editar/{codigo}")
    public String editarProducto(@PathVariable Long codigo, @RequestBody ProductoDb producto) {
        producto.setCodigo(codigo);
        productoService.actualizarProducto(producto);
        return "Producto actualizado exitosamente";
    }
}
