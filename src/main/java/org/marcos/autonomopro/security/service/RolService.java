package org.marcos.autonomopro.security.service;

import java.util.Optional;

import org.marcos.autonomopro.security.entity.RolDb;
import org.marcos.autonomopro.security.enums.RolNombre;
import org.marcos.autonomopro.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional // Mantiene la coherencia de la BD si hay varios accesos
public class RolService {
    
    @Autowired
    RolRepository rolRepository;

    public Optional<RolDb> getByRolNombre(RolNombre rolNombre) {
        return rolRepository.findByNombre(rolNombre);
    }

    public void save(@NonNull RolDb rol) {
        rolRepository.save(rol);
    }
}
