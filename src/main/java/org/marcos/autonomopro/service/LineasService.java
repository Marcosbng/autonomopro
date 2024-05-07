package org.marcos.autonomopro.service;

import java.util.List;

import org.marcos.autonomopro.model.db.LineasDb;
import org.marcos.autonomopro.repository.LineasRepository;
import org.springframework.stereotype.Service;

@Service
public class LineasService {
    private final LineasRepository lineasRepository;

    public LineasService(LineasRepository lineasRepository) {
        this.lineasRepository = lineasRepository;
    }

    public void crearLinea(LineasDb linea) {
        lineasRepository.save(linea);
    }

    public void eliminarLineasPorNumeroFactura(String numeroFactura) {
        // obtener todas las líneas asociadas a la factura por su número
        List<LineasDb> lineas = lineasRepository.findByFacturaNumeroFactura(numeroFactura);
        
        // eliminar cada línea obtenida
        for (LineasDb linea : lineas) {
            lineasRepository.delete(linea);
        }
    }

    public boolean existeProductoEnFactura(Long codigoProducto) {
        // verificar si existe alguna línea que utilice el producto
        return lineasRepository.existsByProductoCodigo(codigoProducto);
    }
}
