package org.marcos.autonomopro.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

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
        // Agregar el título del albarán
        Paragraph titulo = new Paragraph("Albarán - Número: " + albaran.getNumeroAlbaran());
        titulo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        document.add(titulo);

        // Agregar detalles del albarán
        Paragraph detalles = new Paragraph();
        detalles.add(new Paragraph("Detalles del albarán:"));
        detalles.add(new Paragraph("- Fecha: " + albaran.getFecha()));
        detalles.add(new Paragraph("- Número de factura: " + albaran.getNumeroFactura()));
        detalles.add(new Paragraph("- Nombre del cliente: " + albaran.getCliente().getNombre()));

        document.add(detalles);
    }
}
