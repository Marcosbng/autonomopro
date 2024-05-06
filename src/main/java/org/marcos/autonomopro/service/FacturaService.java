package org.marcos.autonomopro.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.repository.FacturasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {
    
    private static final String FORMATO_FECHA = "ddMMyyyy";
    private static final String FORMATO_NUMERO = "%s-%04d";

    private final FacturasRepository facturaRepository;

    @Autowired
    public FacturaService(FacturasRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    // método obtener la lista de facturas
    public List<FacturaDb> getListaFacturas() {
        return facturaRepository.findAll();
    }

    public void crearFactura(FacturaDb factura) {
        // agregar validaciones
        facturaRepository.save(factura);
    }

    // método para modificar una factura existente
    public void modificarFactura(FacturaDb factura) {
        facturaRepository.save(factura);
    }

    // método para obtener una factura por su número
    public FacturaDb obtenerFacturaPorNumero(String numeroFactura) {
        // utiliza el método findByNumeroFactura del repositorio para buscar la factura por su número
        Optional<FacturaDb> optionalFactura = facturaRepository.findByNumeroFactura(numeroFactura);
        // verifica si se encontró la factura y devuelve el resultado, o null si no se encontró
        return optionalFactura.orElse(null);
    }

    // método para eliminar una factura por su número
    public void eliminarFacturaPorNumeroFactura(String numeroFactura) {
        // utiliza findByNumeroFactura buscar factura por su número
        Optional<FacturaDb> optionalFactura = facturaRepository.findByNumeroFactura(numeroFactura);
        // verifica si se encontró la factura y, si es así, la elimina
        optionalFactura.ifPresent(factura -> facturaRepository.delete(factura));
    }

    public List<FacturaDb> obtenerFacturasVencidas(Date fechaActual) {
        // Utiliza el repositorio de facturas para obtener las facturas con fecha de vencimiento anterior a la fecha actual
        return facturaRepository.findByFechaVencimientoBeforeAndEstado(fechaActual, "Pendiente");
    }

    public String generarNumeroFactura() {
        // Obtiene la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        String fechaActual = sdf.format(new Date());
    
        // Busca la última factura creada en la base de datos
        Optional<FacturaDb> ultimaFactura = facturaRepository.findTopByOrderByNumeroFacturaDesc();
    
        int numeroSecuencial;
        if (ultimaFactura.isPresent()) {
            // Obtiene el número secuencial de la última factura y lo incrementa en 1
            String ultimoNumero = ultimaFactura.get().getNumeroFactura().split("-")[1]; // Separa el número de la fecha
            numeroSecuencial = Integer.parseInt(ultimoNumero) + 1;
        } else {
            // Si no hay facturas en la base de datos, comienza desde 1
            numeroSecuencial = 1;
        }
    
        // Formatea el número secuencial con ceros a la izquierda
        String numeroFormateado = String.format(FORMATO_NUMERO, fechaActual, numeroSecuencial);
    
        return numeroFormateado;
    }

    public List<FacturaDb> obtenerFacturasCliente(Long idCliente) {
        return facturaRepository.findByClienteId(idCliente);
    }

}
