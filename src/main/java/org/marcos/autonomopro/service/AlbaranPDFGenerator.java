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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AlbaranPDFGenerator {

    @Autowired
    private TemplateEngine templateEngine;

    public ByteArrayOutputStream generarPDF(AlbaranDb albaran) throws DocumentException {
        Context context = new Context();
        context.setVariable("albaran", albaran);

        String htmlContent = templateEngine.process("albaran", context);

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

