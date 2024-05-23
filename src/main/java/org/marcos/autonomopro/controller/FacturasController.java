package org.marcos.autonomopro.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.service.AlbaranPDFGenerator;
import org.marcos.autonomopro.service.AlbaranService;
import org.marcos.autonomopro.service.ClienteService;
import org.marcos.autonomopro.service.FacturaPDFGenerator;
import org.marcos.autonomopro.service.FacturaService;
import org.marcos.autonomopro.service.LineasService;
import org.marcos.autonomopro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class FacturasController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AlbaranService albaranService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private LineasService lineasService;

    @Autowired
    private FacturaPDFGenerator facturaPDFGenerator;

    @GetMapping("/listarFacturas")
    public String listarFacturas(Model model, @RequestParam(defaultValue = "numeroFactura") String orderBy,
            @RequestParam(required = false) String buscarPor) {
        List<FacturaDb> listaFacturas;
        if (buscarPor != null && !buscarPor.isEmpty()) {
            listaFacturas = facturaService.buscarFacturas(buscarPor);
        } else {
            listaFacturas = facturaService.getListaFacturas(orderBy);
        }
        model.addAttribute("facturas", listaFacturas);
        return "listaFacturas";
    }

    @GetMapping("/crearFactura")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("factura", new FacturaDb());
        model.addAttribute("clientes", clienteService.getListaClientes()); // agregar la lista de clientes
        model.addAttribute("productos", productoService.getListaProductos()); // agregar la lista de productos

        return "formularioFactura";
    }

    @PostMapping("/crearFactura")
    public String crearFactura(@ModelAttribute("factura") FacturaDb factura, @RequestParam("clienteId") Long clienteId,
            @RequestParam("productos") List<Long> productosCodigos,
            @RequestParam("cantidades") List<Integer> cantidades,
            BindingResult result, RedirectAttributes attributes) {

        // obtener el cliente por su id
        ClienteDb cliente = clienteService.obtenerClientePorId(clienteId);

        // asignar el cliente y el estado a la factura
        factura.setNumeroFactura(facturaService.generarNumeroFactura());
        factura.setCliente(cliente);
        factura.setEstado("Pendiente");

        // calcular importes y asignarlos a la factura
        facturaService.calcularYAsignarImportes(factura, productosCodigos, cantidades);

        facturaService.crearFactura(factura);

        // crear la factura y guardarla en la base de datos
        facturaService.crearFacturaConLineas(factura, productosCodigos, cantidades);

        attributes.addFlashAttribute("mensaje", "Factura creada exitosamente");
        return "redirect:/listarFacturas";
    }

    @GetMapping("/visualizar/factura/{numeroFactura}")
    public String visualizarFactura(@PathVariable String numeroFactura, Model model) {
        FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);
        model.addAttribute("factura", factura);
        return "visualizarFactura"; // vista de la factura
    }

    @GetMapping("/eliminar/factura/{numeroFactura}")
    public String mostrarFormularioEliminacion(@PathVariable String numeroFactura, Model model) {
        // obtener la factura por su número
        FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);
        // agregar la factura al modelo
        model.addAttribute("factura", factura);
        // vista de confirmación de eliminación
        return "formEliminarFactura";
    }

    @PostMapping("/eliminar/factura/{numeroFactura}")
    public String eliminarFactura(@PathVariable String numeroFactura, RedirectAttributes attributes) {
        // obtener la factura por su número
        FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);

        // verificar si la factura tiene un número de albarán asociado
        if (factura.getAlbaranNumero() != null) {
            // Si la factura tiene un número de albarán asociado, redirigir con un mensaje
            // de error
            attributes.addFlashAttribute("mensajeError",
                    "La factura no se puede eliminar porque ya tiene un albarán asociado!");
            return "redirect:/listarFacturas";
        }

        // eliminar las líneas asociadas a esta factura
        lineasService.eliminarLineasPorNumeroFactura(numeroFactura);

        // eliminar la factura
        facturaService.eliminarFacturaPorNumeroFactura(numeroFactura);

        attributes.addFlashAttribute("mensaje", "Factura eliminada exitosamente");
        return "redirect:/listarFacturas";
    }

    @GetMapping("/pagarFactura/{id}")
    public String mostrarConfirmacionPago(@PathVariable("id") String numeroFactura, Model model) {

        model.addAttribute("id", numeroFactura);
        return "pagarFactura"; // vista de confirmación de pago
    }

    @PostMapping("/pagarFactura/{id}")
    public String pagarFactura(@PathVariable("id") String numeroFactura) {
        FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);
        if (factura != null) {
            // marcar la factura como pagada
            factura.setEstado("Pagada");
            facturaService.modificarFactura(factura);

            // crear un nuevo albarán
            AlbaranDb albaran = new AlbaranDb();
            // configurar los detalles del albarán según la factura
            albaran.setNumeroAlbaran(albaranService.generarNumeroAlbaran()); // generamos número albarán
            albaran.setFecha(new Date(System.currentTimeMillis())); // fecha actual
            albaran.setCliente(factura.getCliente()); // cliente de la factura
            albaran.setFactura(factura);

            // guardar el albarán en la base de datos
            albaranService.crearAlbaran(albaran);

            factura.setAlbaranNumero(albaran.getNumeroAlbaran()); // no lo pongo arriba para optimizar simplemente
                                                                  // porque quiero que siga la estructura de
            facturaService.modificarFactura(factura); // primero pagar factura, generar el albaran y luego modificar
                                                      // numero albaran en la factura
        }
        return "redirect:/visualizar/factura/" + numeroFactura;
    }

    @GetMapping("/facturas/cliente/{id}")
    public String mostrarFacturasCliente(@PathVariable("id") Long id, Model model) {
        // obtener facturas del cliente
        List<FacturaDb> facturas = facturaService.obtenerFacturasCliente(id);
        model.addAttribute("facturas", facturas);
        return "listaFacturasCliente"; // vista para mostrar las facturas del cliente
    }

    @GetMapping("/cantidadFacturasHistorico")
    public ResponseEntity<Map<String, Long>> getCantidadFacturasHistorico() {
        List<Object[]> results = facturaService.getCantidadFacturasHistorico();
        Map<String, Long> historico = new HashMap<>();
        for (Object[] result : results) {
            String fecha = (String) result[0];
            Long cantidad = ((Number) result[1]).longValue();
            historico.put(fecha, cantidad);
        }
        return ResponseEntity.ok().body(historico);
    }

    @GetMapping("/importeTotalFacturasHistorico")
    public ResponseEntity<Map<String, Double>> getImporteTotalFacturasHistorico() {
        List<Object[]> results = facturaService.getImporteTotalFacturasHistorico();
        Map<String, Double> historico = new HashMap<>();
        for (Object[] result : results) {
            String fecha = (String) result[0];
            Double importe = ((Number) result[1]).doubleValue();
            historico.put(fecha, importe);
        }
        return ResponseEntity.ok().body(historico);
    }

    @GetMapping("/descargarFactura/{numeroFactura}")
    public void descargarFactura(@PathVariable String numeroFactura, HttpServletResponse response) throws IOException {
        try {
            // obtener el objeto FacturaDb correspondiente al número de factura proporcionado
            FacturaDb factura = facturaService.obtenerFacturaPorNumero(numeroFactura);

            // generar el contenido de la factura en un documento PDF utilizando el objeto FacturaDb
            ByteArrayOutputStream baos = facturaPDFGenerator.generarPDF(factura);

            // establecer las cabeceras de la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=factura_" + numeroFactura + ".pdf");

            // escribir el contenido del PDF en el flujo de salida de la respuesta
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el documento PDF de la factura", e);
        }
    }

}