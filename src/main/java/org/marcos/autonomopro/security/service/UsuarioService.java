package org.marcos.autonomopro.security.service;

import java.util.Optional;

import org.marcos.autonomopro.security.entity.UsuarioDb;
import org.marcos.autonomopro.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<UsuarioDb> getByNickname(String nickname) {
        return usuarioRepository.findByNickname(nickname);
    }

    public boolean existsByNickname(String nickname) {
        return usuarioRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void save(@NonNull UsuarioDb usuario) {
        usuarioRepository.save(usuario);
    }
}
