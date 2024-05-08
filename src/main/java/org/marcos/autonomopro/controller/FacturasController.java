package org.marcos.autonomopro.controller;

import java.sql.Date;
import java.util.List;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.model.db.ClienteDb;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.marcos.autonomopro.model.db.ProductoDb;
import org.marcos.autonomopro.service.AlbaranService;
import org.marcos.autonomopro.service.ClienteService;
import org.marcos.autonomopro.service.FacturaService;
import org.marcos.autonomopro.service.LineasService;
import org.marcos.autonomopro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/listarFacturas")
    public String listarFacturas(Model model) {
        List<FacturaDb> listaFacturas = facturaService.getListaFacturas();
        model.addAttribute("facturas", listaFacturas);
        return "listaFacturas"; // nombre de la vista que mostrará la lista de facturas
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
        @RequestParam("productos") List<Long> productosCodigos, @RequestParam("cantidades") List<Integer> cantidades,
        @RequestParam("importeTotal") float importeTotal, BindingResult result, RedirectAttributes attributes) {

    // obtener el cliente por su id
    ClienteDb cliente = clienteService.obtenerClientePorId(clienteId);

    // asignar el cliente y el estado a la factura
    factura.setNumeroFactura(facturaService.generarNumeroFactura());
    factura.setCliente(cliente);
    factura.setEstado("Pendiente");

    // calcular el importe total del IVA (21%)
    float importeTotalIVA = importeTotal * 0.21f;
    factura.setImporteTotalIVA(importeTotalIVA);

    // calcular el importe total a pagar (importe total + importe total del IVA)
    float importeTotalAPagar = importeTotal + importeTotalIVA;
    factura.setImporteTotalAPagar(importeTotalAPagar);

    // crear la factura y guardarla en la base de datos
    facturaService.crearFactura(factura);

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
            albaran.setNumeroFactura(factura.getNumeroFactura());

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

    // Modificar factura que no este aun pagada con su albaran
    // productos

}