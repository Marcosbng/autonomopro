package org.marcos.autonomopro.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class FacturaPDFGenerator {

    public ByteArrayOutputStream generarPDF(FacturaDb factura) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            agregarContenidoFactura(document, factura);
        } finally {
            document.close();
        }
        return baos;
    }

    private void agregarContenidoFactura(Document document, FacturaDb factura) throws DocumentException {
        // Agregar logo
        try {
            Image logo = Image.getInstance("src/main/resources/static/dist/img/autonomo-pro.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Titulo
        Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Factura - Número: " + factura.getNumeroFactura(), fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        // Detalles de la factura
        Font fontDetalles = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font fontDetallesBold = new Font(Font.HELVETICA, 12, Font.BOLD);

        PdfPTable tablaDetalles = new PdfPTable(2);
        tablaDetalles.setWidthPercentage(100);
        tablaDetalles.setSpacingBefore(20f);
        tablaDetalles.setSpacingAfter(20f);

        tablaDetalles.addCell(new Phrase("Fecha de Emisión:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(factura.getFechaEmision().toString(), fontDetalles));
        tablaDetalles.addCell(new Phrase("Fecha de Vencimiento:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(factura.getFechaVencimiento().toString(), fontDetalles));
        tablaDetalles.addCell(new Phrase("Estado:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(factura.getEstado(), fontDetalles));
        tablaDetalles.addCell(new Phrase("Nombre del Cliente:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(factura.getCliente().getNombre(), fontDetalles));
        tablaDetalles.addCell(new Phrase("Importe Total:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(String.valueOf(factura.getImporteTotal()), fontDetalles));
        tablaDetalles.addCell(new Phrase("Importe Total IVA:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(String.valueOf(factura.getImporteTotalIVA()), fontDetalles));
        tablaDetalles.addCell(new Phrase("Importe Total a Pagar:", fontDetallesBold));
        tablaDetalles.addCell(new Phrase(String.valueOf(factura.getImporteTotalAPagar()), fontDetalles));

        document.add(tablaDetalles);

        // Tabla de productos
        Paragraph productosTitulo = new Paragraph("Productos:", fontDetallesBold);
        productosTitulo.setSpacingBefore(10f);
        productosTitulo.setSpacingAfter(10f);
        document.add(productosTitulo);

        PdfPTable tablaProductos = new PdfPTable(3);
        tablaProductos.setWidthPercentage(100);
        tablaProductos.setSpacingBefore(10f);
        tablaProductos.setSpacingAfter(10f);

        tablaProductos.addCell(new Phrase("Producto", fontDetallesBold));
        tablaProductos.addCell(new Phrase("Cantidad", fontDetallesBold));
        tablaProductos.addCell(new Phrase("Precio Unitario", fontDetallesBold));

        List<LineasDb> lineasFactura = factura.getLineasFactura();
        for (LineasDb linea : lineasFactura) {
            tablaProductos.addCell(new Phrase(linea.getProducto().getNombre(), fontDetalles));
            tablaProductos.addCell(new Phrase(String.valueOf(linea.getCantidadProducto()), fontDetalles));
            tablaProductos.addCell(new Phrase(String.valueOf(linea.getProducto().getPrecioUnitario()), fontDetalles));
        }

        document.add(tablaProductos);

        // Política de privacidad
        Paragraph politicaPrivacidad = new Paragraph("\nPolítica de Privacidad:", fontDetallesBold);
        politicaPrivacidad.add(new Paragraph(
                "La información proporcionada en esta factura es confidencial y se destina únicamente ", fontDetalles));
        politicaPrivacidad.add(new Paragraph(
                "para el uso del destinatario. Cualquier divulgación, reproducción o distribución ", fontDetalles));
        politicaPrivacidad.add(new Paragraph(
                "de esta factura sin autorización está estrictamente prohibida. ", fontDetalles));

        document.add(politicaPrivacidad);
    }
}
