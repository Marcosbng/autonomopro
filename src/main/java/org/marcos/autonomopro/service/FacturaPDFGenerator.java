package org.marcos.autonomopro.service;

import com.lowagie.text.DocumentException;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Component
public class FacturaPDFGenerator {

    @Autowired
    private TemplateEngine templateEngine;

    public ByteArrayOutputStream generarPDF(FacturaDb factura) throws DocumentException {
        Context context = new Context();
        context.setVariable("factura", factura);

        String htmlContent = templateEngine.process("factura", context);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(baos);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
        return baos;
    }
}
