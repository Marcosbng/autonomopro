package org.marcos.autonomopro.service;

import java.util.List;
import java.util.Optional;

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
}
