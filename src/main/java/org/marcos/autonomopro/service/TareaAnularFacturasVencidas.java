package org.marcos.autonomopro.service;

import java.util.Date;
import java.util.List;

import org.marcos.autonomopro.model.db.FacturaDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TareaAnularFacturasVencidas {

    private static final Logger logger = LoggerFactory.getLogger(TareaAnularFacturasVencidas.class);

    @Autowired
    private FacturaService facturaService;

    // Método que se ejecutará automáticamente cada día a la medianoche
    @Scheduled(cron = "0 0 * * * ?")
    public void anularFacturasVencidas() {
        logger.info("Ejecutando tarea de anulación de facturas vencidas...");
        
        // Obtener la lista de facturas vencidas
        List<FacturaDb> facturasVencidas = facturaService.obtenerFacturasVencidas(new Date(System.currentTimeMillis()));
        
        logger.info("Número de facturas vencidas encontradas: {}", facturasVencidas.size());

        // Iterar sobre las facturas vencidas y anularlas
        for (FacturaDb factura : facturasVencidas) {
            logger.info("Anulando factura vencida con número: {}", factura.getNumeroFactura());
            factura.setEstado("Anulada");
            facturaService.modificarFactura(factura);
            logger.info("Factura anulada con éxito");
        }

        logger.info("Tarea de anulación de facturas vencidas completada.");
    }
}
