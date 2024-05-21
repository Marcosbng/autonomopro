package org.marcos.autonomopro.service;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.repository.FacturasRepository;
import org.marcos.autonomopro.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {

    private static final String FORMATO_FECHA = "ddMMyyyy";
    private static final String FORMATO_NUMERO = "%s-%04d";

    @Autowired
    private ProductoService productoService;

    @Autowired
    private LineasService lineasService;

    private final FacturasRepository facturaRepository;

    @Autowired
    public FacturaService(FacturasRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public List<FacturaDb> getListaFacturas(String orderBy) {
        Sort sort = Sort.by(orderBy).ascending(); // ascendente por defecto
        return facturaRepository.findAll(sort);
    }

    public List<FacturaDb> buscarFacturas(String buscarPor) {
        return facturaRepository.findByNumeroFacturaContaining(buscarPor);
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
        // utiliza el método findByNumeroFactura del repositorio para buscar la factura
        // por su número
        Optional<FacturaDb> optionalFactura = facturaRepository.findByNumeroFactura(numeroFactura);
        // verifica si se encontró la factura y devuelve el resultado, o null si no se
        // encontró
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
        // utiliza el repositorio de facturas para obtener las facturas con fecha de
        // vencimiento anterior a la fecha actual
        return facturaRepository.findByFechaVencimientoBeforeAndEstado(fechaActual, "Pendiente");
    }

    public String generarNumeroFactura() {
        // obtiene la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        String fechaActual = sdf.format(new Date());

        // busca la última factura creada en la base de datos
        Optional<FacturaDb> ultimaFactura = facturaRepository.findTopByOrderByNumeroFacturaDesc();

        int numeroSecuencial;
        if (ultimaFactura.isPresent()) {
            // obtiene el número secuencial de la última factura y lo incrementa en 1
            String ultimoNumero = ultimaFactura.get().getNumeroFactura().split("-")[1]; // Separa el número de la fecha
            numeroSecuencial = Integer.parseInt(ultimoNumero) + 1;
        } else {
            // si no hay facturas en la base de datos, comienza desde 1
            numeroSecuencial = 1;
        }

        // formatea el número secuencial con ceros a la izquierda
        String numeroFormateado = String.format(FORMATO_NUMERO, fechaActual, numeroSecuencial);

        return numeroFormateado;
    }

    public List<FacturaDb> obtenerFacturasCliente(Long idCliente) {
        return facturaRepository.findByClienteId(idCliente);
    }

    public float calcularImporteTotal(List<Long> productosCodigos, List<Integer> cantidades) {
        float importeTotal = 0.0f;
        for (int i = 0; i < productosCodigos.size(); i++) {
            Long codigoProducto = productosCodigos.get(i);
            Integer cantidad = cantidades.get(i);
            float precioProducto = productoService.obtenerPrecioProducto(codigoProducto);
            importeTotal += cantidad * precioProducto;
        }
        return importeTotal;
    }

    public void calcularYAsignarImportes(FacturaDb factura, List<Long> productosCodigos, List<Integer> cantidades) {
        // calcular y asignar el importe total
        float importeTotal = calcularImporteTotal(productosCodigos, cantidades);
        factura.setImporteTotal(importeTotal);
        // calcular el importe total del IVA (21%)
        float importeTotalIVA = importeTotal * 0.21f;
        factura.setImporteTotalIVA(importeTotalIVA);
        // calcular el importe total a pagar (importe total + importe total del IVA)
        float importeTotalAPagar = importeTotal + importeTotalIVA;
        factura.setImporteTotalAPagar(importeTotalAPagar);
    }

    public void crearFacturaConLineas(FacturaDb factura, List<Long> productosCodigos, List<Integer> cantidades) {

        // asignar los productos y sus cantidades a la factura
        for (int i = 0; i < productosCodigos.size(); i++) {
            Long productoCodigo = productosCodigos.get(i);
            Integer cantidad = cantidades.get(i);

            ProductoDb producto = productoService.obtenerProductoPorCodigo(productoCodigo);
            LineasDb linea = new LineasDb();
            linea.setFactura(factura);
            linea.setProducto(producto);
            linea.setCantidadProducto(cantidad);
            // guardar la línea de factura en la base de datos
            lineasService.crearLinea(linea);
        }
    }

    public List<Object[]> getCantidadFacturasHistorico() {
        return facturaRepository.findCantidadFacturasGroupByDia();
    }

    public List<Object[]> getImporteTotalFacturasHistorico() {
        return facturaRepository.findImporteTotalFacturasGroupByDia();
    }
    
}