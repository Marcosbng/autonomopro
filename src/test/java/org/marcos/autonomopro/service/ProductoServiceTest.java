package org.marcos.autonomopro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.repository.ProductosRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductosRepository productosRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testCrearProducto() {
        ProductoDb producto = new ProductoDb();

        productoService.crearProducto(producto);
        verify(productosRepository, times(1)).save(producto);
    }

    @Test
    void testGetListaProductos() {
        List<ProductoDb> productos = new ArrayList<>();
        productos.add(new ProductoDb());
        productos.add(new ProductoDb());

        when(productosRepository.findAll()).thenReturn(productos);

        List<ProductoDb> listaProductos = productoService.getListaProductos();
        assertEquals(2, listaProductos.size());
    }

    @Test
    void testObtenerProductoPorCodigo() {
        Long codigo = 12345L;
        ProductoDb producto = new ProductoDb();
        producto.setCodigo(codigo);

        when(productosRepository.findByCodigo(codigo)).thenReturn(Optional.of(producto));

        ProductoDb productoObtenido = productoService.obtenerProductoPorCodigo(codigo);
        assertNotNull(productoObtenido);
        assertEquals(codigo, productoObtenido.getCodigo());
    }

    @Test
    void eliminarProductoPorCodigo_ProductoExistente_DeberiaEliminarlo() {
        // Arrange
        Long codigoProducto = 123L;
        doNothing().when(productosRepository).deleteById(codigoProducto);

        // Act
        productoService.eliminarProductoPorCodigo(codigoProducto);

        // Assert
        verify(productosRepository, times(1)).deleteById(codigoProducto);
    }

    @Test
    void actualizarProducto_ProductoExistente_DeberiaActualizarlo() {
        // Arrange
        ProductoDb producto = new ProductoDb();
        producto.setCodigo(123L);
        producto.setNombre("Producto de prueba");
        producto.setPrecioUnitario(10.0f);
        producto.setIva(0.21f);
        
        when(productosRepository.save(any(ProductoDb.class))).thenReturn(producto);

        // Act
        productoService.actualizarProducto(producto);

        // Assert
        verify(productosRepository, times(1)).save(producto);
    }
}
