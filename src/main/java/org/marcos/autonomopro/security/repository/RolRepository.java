package org.marcos.autonomopro.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.marcos.autonomopro.security.entity.RolDb;
import org.marcos.autonomopro.security.enums.RolNombre;

public interface RolRepository extends JpaRepository<RolDb, Integer>{
    Optional<RolDb> findByNombre(RolNombre rolNombre);
}
