package org.marcos.autonomopro.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class FacturaPDFGenerator {

    public ByteArrayOutputStream generarPDF(FacturaDb factura) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarContenidoFactura(document, factura);
        } finally {
            document.close();
        }
        return baos;
    }

    private void agregarContenidoFactura(Document document, FacturaDb factura) throws DocumentException {
        Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Factura - Número: " + factura.getNumeroFactura(), fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        // Detalles de la factura
        Font fontDetalles = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph detalles = new Paragraph("Detalles de la factura:", fontDetalles);
        detalles.setSpacingAfter(10f);

        // Detalles específicos de la factura
        detalles.add(new Paragraph("- Fecha: " + factura.getFechaEmision()));
        detalles.add(new Paragraph("- Fecha de vencimiento: " + factura.getFechaVencimiento()));
        detalles.add(new Paragraph("- Estado: " + factura.getEstado()));
        detalles.add(new Paragraph("- Nombre del cliente: " + factura.getCliente().getNombre()));
        detalles.add(new Paragraph("- Importe total: " + factura.getImporteTotal()));
        detalles.add(new Paragraph("- Importe total IVA: " + factura.getImporteTotalIVA()));
        detalles.add(new Paragraph("- Importe total a pagar: " + factura.getImporteTotalAPagar()));

        // Productos
        Paragraph productos = new Paragraph("- Productos:", fontDetalles);
        List<LineasDb> lineasFactura = factura.getLineasFactura();
        for (LineasDb linea : lineasFactura) {
            String detalleProducto = String.format("  - %s: %d unidades",
                    linea.getProducto().getNombre(), linea.getCantidadProducto());
            productos.add(new Paragraph(detalleProducto));
        }
        detalles.add(productos);

        // Política de privacidad
        Font fontPolitica = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Paragraph politicaPrivacidad = new Paragraph("\nPolítica de Privacidad:", fontDetalles);
        politicaPrivacidad.add(new Paragraph(
                "La información proporcionada en esta factura es confidencial y se destina únicamente ", fontPolitica));
        politicaPrivacidad.add(new Paragraph(
                "para el uso del destinatario. Cualquier divulgación, reproducción o distribución ", fontPolitica));
        politicaPrivacidad.add(new Paragraph(
                "de esta factura sin autorización está estrictamente prohibida. ", fontPolitica));

        document.add(detalles);
        document.add(politicaPrivacidad);
    }  
}
