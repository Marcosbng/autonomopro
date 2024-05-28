package org.marcos.autonomopro.security.repository;

import java.util.Optional;

import org.marcos.autonomopro.security.entity.UsuarioDb;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UsuarioRepository extends JpaRepository<UsuarioDb,Long  /*Integer*/>{
    Optional<UsuarioDb> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
