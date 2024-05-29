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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public ResponseEntity<String> crearFactura(@RequestBody FacturaDb factura,
                                               @RequestParam("clienteId") Long clienteId,
                                               @RequestParam("productos") List<Long> productosCodigos,
                                               @RequestParam("cantidades") List<Integer> cantidades) {
        try {
            // Obtener el cliente por su ID
            ClienteDb cliente = clienteService.obtenerClientePorId(clienteId);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }

            // Asignar el cliente y el estado a la factura
            factura.setNumeroFactura(facturaService.generarNumeroFactura());
            factura.setCliente(cliente);
            factura.setEstado("Pendiente");

            // Calcular importes y asignarlos a la factura
            facturaService.calcularYAsignarImportes(factura, productosCodigos, cantidades);

            // Crear la factura y guardarla en la base de datos
            facturaService.crearFacturaConLineas(factura, productosCodigos, cantidades);

            return ResponseEntity.status(HttpStatus.CREATED).body("Factura creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la factura: " + e.getMessage());
        }
    }

    @GetMapping("/visualizar/{numeroFactura}")
    public FacturaDb visualizarFactura(@PathVariable String numeroFactura) {
        return facturaService.obtenerFacturaPorNumero(numeroFactura);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
