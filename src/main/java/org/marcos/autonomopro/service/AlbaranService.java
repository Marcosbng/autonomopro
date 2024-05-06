package org.marcos.autonomopro.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.repository.AlbaranRepository;
import org.marcos.autonomopro.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbaranService {

    @Autowired
    private AlbaranRepository albaranRepository;

    // método obtener lista de albaranes
    public List<AlbaranDb> getListaAlbaranes() {
        return albaranRepository.findAll();
    }

    // método para crear un albarán
    public void crearAlbaran(AlbaranDb albaran) {
        albaranRepository.save(albaran);
    }

    // método para eliminar un albarán por su número de albarán
    public void eliminarAlbaranPorNumeroAlbaran(String numeroAlbaran) {

        AlbaranDb albaran = albaranRepository.findByNumeroAlbaran(numeroAlbaran);

        if (albaran != null) {
            albaranRepository.delete(albaran);
        } else {
            System.err.println("No se encontró ningún albarán con el número proporcionado: " + numeroAlbaran);
        }
    }

    // método para generar un número de albarán único
    public String generarNumeroAlbaran() {
        // Genera un UUID (Identificador Único Universal) como número de albarán
        String numeroAlbaran = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        return numeroAlbaran;
    }

    public AlbaranDb obtenerAlbaranPorNumero(String numeroAlbaran) {
        return albaranRepository.findByNumeroAlbaran(numeroAlbaran);
    }

}
