package org.marcos.autonomopro.service;

import java.util.List;
import java.util.Optional;

import org.marcos.autonomopro.exception.ProductoNotFoundException;
import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.repository.ClientesRepository;
import org.marcos.autonomopro.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    
    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private LineasService lineasService;

    // método obtener lista productos
    public List<ProductoDb> getListaProductos() {
        return productosRepository.findAll();
    }

    // método crear nuevo producto
    public void crearProducto(ProductoDb producto) {
        // Agregar validaciones u lógica de negocio si es necesario
        productosRepository.save(producto);
    }

    // método obtener un cliente por su ID
    public ProductoDb obtenerProductoPorCodigo(Long codigo) {
        Optional<ProductoDb> productoOptional = productosRepository.findByCodigo(codigo);
        return productoOptional.orElse(null);
    }

    public boolean eliminarProductoPorCodigo(Long codigo) {
        boolean enUso = lineasService.existeProductoEnFactura(codigo);
        if (!enUso) {
            productosRepository.deleteById(codigo);
            return true;
        } else {
            return false;
        }
    }
    
    // método actualizar producto
    public void actualizarProducto(ProductoDb producto) {
        productosRepository.save(producto);
    }

    public float obtenerPrecioProducto(Long codigoProducto) {
        Optional<ProductoDb> optionalProducto = productosRepository.findByCodigo(codigoProducto);
        if (optionalProducto.isPresent()) {
            ProductoDb producto = optionalProducto.get();
            return producto.getPrecioUnitario();
        } else {
            throw new ProductoNotFoundException("No se encontró el producto con el código: " + codigoProducto);
        }
    }
}
