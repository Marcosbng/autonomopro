package org.marcos.autonomopro.security.service;

import java.util.Optional;

import org.marcos.autonomopro.security.dto.UsuarioEdit;

public interface UsuarioInterface {
    public Optional<UsuarioEdit> getUsuarioEditNickname(String nickname);
}
