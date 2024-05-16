package org.marcos.autonomopro.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.model.db.LineasDb;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class AlbaranPDFGenerator {

    public ByteArrayOutputStream generarPDF(AlbaranDb albaran) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarContenidoAlbaran(document, albaran);
        } finally {
            document.close();
        }
        return baos;
    }

    private void agregarContenidoAlbaran(Document document, AlbaranDb albaran) throws DocumentException {
        Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Albarán - Número: " + albaran.getNumeroAlbaran(), fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20f);
        document.add(titulo);
    
        // Detalles del albarán
        Font fontDetalles = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph detalles = new Paragraph("Detalles del albarán:", fontDetalles);
        detalles.setSpacingAfter(10f);
    
        // Detalles específicos del albarán
        detalles.add(new Paragraph("- Fecha: " + albaran.getFecha()));
        detalles.add(new Paragraph("- Número de factura: " + albaran.getFactura().getNumeroFactura()));
        detalles.add(new Paragraph("- Nombre del cliente: " + albaran.getCliente().getNombre()));
        detalles.add(new Paragraph("- Importe total: " + albaran.getFactura().getImporteTotal()));
        detalles.add(new Paragraph("- Importe total iva: " + albaran.getFactura().getImporteTotalIVA()));
        detalles.add(new Paragraph("- Importe total a pagar: " + albaran.getFactura().getImporteTotalAPagar()));
    
        // Productos
        Paragraph productos = new Paragraph("- Productos:", fontDetalles);
        List<LineasDb> lineasFactura = albaran.getFactura().getLineasFactura();
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
                "La información proporcionada en este albarán es confidencial y se destina únicamente ", fontPolitica));
        politicaPrivacidad.add(new Paragraph(
                "para el uso del destinatario. Cualquier divulgación, reproducción o distribución ", fontPolitica));
        politicaPrivacidad.add(new Paragraph(
                "de este albarán sin autorización está estrictamente prohibida. ", fontPolitica));
    
        document.add(detalles);
        document.add(politicaPrivacidad);
    
        // Imagen
        try {
            Image imagen = Image.getInstance("src/main/resources/static/dist/img/autonomo-pro.png");
            imagen.scaleToFit(100, 100);
            imagen.setAlignment(Element.ALIGN_CENTER);
            document.add(imagen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}

