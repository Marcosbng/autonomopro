package org.marcos.autonomopro.controller;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.service.AlbaranPDFGenerator;
import org.marcos.autonomopro.service.AlbaranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/albaranes")
public class AlbaranesRestController {

    @Autowired
    private AlbaranService albaranService;

    @Autowired
    private AlbaranPDFGenerator albaranPDFGenerator;

    @GetMapping("/listar")
    public List<AlbaranDb> listarAlbaranes() {
        return albaranService.getListaAlbaranes();
    }

    @GetMapping("/descargar/{numeroAlbaran}")
    public void descargarAlbaran(@PathVariable String numeroAlbaran, HttpServletResponse response) throws IOException {
        try {
            AlbaranDb albaran = albaranService.obtenerAlbaranPorNumero(numeroAlbaran);
            ByteArrayOutputStream baos = albaranPDFGenerator.generarPDF(albaran);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=albaran_" + numeroAlbaran + ".pdf");

            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new IOException("Error al generar el documento PDF del albarán", e);
        }
    }
}
