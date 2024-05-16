package org.marcos.autonomopro.controller;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.service.AlbaranService;
import org.marcos.autonomopro.service.ClienteService;
import org.marcos.autonomopro.service.FacturaService;
import org.marcos.autonomopro.service.LineasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturasRestController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private AlbaranService albaranService;

    @Autowired
    private LineasService lineasService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listar")
    public List<FacturaDb> listarFacturas(@RequestParam(defaultValue = "numeroFactura") String orderBy,
            @RequestParam(required = false) String buscarPor) {
        if (buscarPor != null && !buscarPor.isEmpty()) {
            return facturaService.buscarFacturas(buscarPor);
        } else {
            return facturaService.getListaFacturas(orderBy);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<FacturaDb> crearFactura(@RequestBody FacturaDb factura,
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("productos") List<Long> productosCodigos,
            @RequestParam("cantidades") List<Integer> cantidades) {

        try {
            ClienteDb cliente = clienteService.obtenerClientePorId(clienteId);

            factura.setCliente(cliente);
            factura.setEstado("Pendiente");

            facturaService.calcularYAsignarImportes(factura, productosCodigos, cantidades);

            factura.setNumeroFactura(facturaService.generarNumeroFactura());
            facturaService.crearFacturaConLineas(factura, productosCodigos, cantidades);

            return ResponseEntity.status(HttpStatus.CREATED).body(factura);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/visualizar/{numeroFactura}")
    public FacturaDb visualizarFactura(@PathVariable String numeroFactura) {
        return facturaService.obtenerFacturaPorNumero(numeroFactura);
    }

    @DeleteMapping("/eliminar/{numeroFactura}")
    public String eliminarFactura(@PathVariable String numeroFactura) {
        try {
            lineasService.eliminarLineasPorNumeroFactura(numeroFactura);

            FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);

            if (factura.getAlbaranNumero() != null) {
                return "No se puede eliminar la factura porque ya tiene un albarán asociado";
            }

            facturaService.eliminarFacturaPorNumeroFactura(numeroFactura);

            return "Factura eliminada exitosamente";
        } catch (Exception e) {
            return "Error al eliminar la factura: " + e.getMessage();
        }
    }

    @PostMapping("/pagar/{numeroFactura}")
    public FacturaDb pagarFactura(@PathVariable String numeroFactura) {
        FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);
        if (factura != null) {
            factura.setEstado("Pagada");
            facturaService.modificarFactura(factura);
            AlbaranDb albaran = new AlbaranDb();
            albaran.setNumeroAlbaran(albaranService.generarNumeroAlbaran());
            albaran.setFecha(new Date(System.currentTimeMillis()));
            albaran.setCliente(factura.getCliente());
            albaran.setFactura(factura);
            albaranService.crearAlbaran(albaran);
            factura.setAlbaranNumero(albaran.getNumeroAlbaran());
            facturaService.modificarFactura(factura);
        }
        return factura;
    }

    @GetMapping("/cliente/{id}")
    public List<FacturaDb> mostrarFacturasCliente(@PathVariable("id") Long id) {
        return facturaService.obtenerFacturasCliente(id);
    }
}
