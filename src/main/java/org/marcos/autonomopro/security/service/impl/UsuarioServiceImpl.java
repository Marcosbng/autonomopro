package org.marcos.autonomopro.security.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import org.marcos.autonomopro.security.dto.UsuarioEdit;
import org.marcos.autonomopro.security.entity.UsuarioDb;
import org.marcos.autonomopro.security.repository.UsuarioRepository;
import org.marcos.autonomopro.security.service.UsuarioInterface;
import org.marcos.autonomopro.security.service.mapper.UsuarioMapper;

@Service
public class UsuarioServiceImpl implements UsuarioInterface {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<UsuarioEdit> getUsuarioEditNickname(String nickname) {
        Optional<UsuarioDb> usuarioDb = usuarioRepository.findByNickname(nickname);

        if (usuarioDb.isPresent()) {
            return Optional.of(UsuarioMapper.INSTANCE.usuarioDbToUsuarioEdit(usuarioDb.get()));
        } else {
            return Optional.empty();
        }
    }

}
