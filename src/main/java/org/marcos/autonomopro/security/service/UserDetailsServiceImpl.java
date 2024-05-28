package org.marcos.autonomopro.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.marcos.autonomopro.security.entity.UsuarioDb;
import org.marcos.autonomopro.security.entity.UsuarioPrincipal;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        //Método que debemos sobreescribir (debe tener este nombre) de la interfaz UserDetaisService.
        //En nuestro caso buscamos por nickname en la BD y devolvemos un UsuarioPrincipal,
        //que es una implementacion de la interfaz UserDetails.
        UsuarioDb usuario = usuarioService.getByNickname(nickname).get();
        return UsuarioPrincipal.build(usuario);
    }
}
