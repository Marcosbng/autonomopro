package org.marcos.autonomopro.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.service.AlbaranPDFGenerator;
import org.marcos.autonomopro.service.AlbaranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AlbaranesController {

    @Autowired
    private AlbaranService albaranService;

    @Autowired
    private AlbaranPDFGenerator albaranPDFGenerator;

    @GetMapping("/listarAlbaranes")
    public String listarAlbaranes(Model model) {
        List<AlbaranDb> listaAlbaranes = albaranService.getListaAlbaranes();
        model.addAttribute("albaranes", listaAlbaranes);
        return "listaAlbaranes"; // vista que mostrará la lista de albaranes
    }

    @GetMapping("/descargarAlbaran/{numeroAlbaran}")
    public void descargarAlbaran(@PathVariable String numeroAlbaran, HttpServletResponse response) throws IOException {
        try {
            // obtener el objeto AlbaranDb correspondiente al número de albarán
            // proporcionado
            AlbaranDb albaran = albaranService.obtenerAlbaranPorNumero(numeroAlbaran);

            // generar el contenido del albarán en un documento PDF utilizando el objeto
            // AlbaranDb
            ByteArrayOutputStream baos = albaranPDFGenerator.generarPDF(albaran);

            // establecer las cabeceras de la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=albaran_" + numeroAlbaran + ".pdf");

            // escribir el contenido del PDF en el flujo de salida de la respuesta
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el documento PDF del albarán", e);
        }
    }

}
