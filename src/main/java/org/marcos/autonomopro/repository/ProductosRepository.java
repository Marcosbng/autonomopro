package org.marcos.autonomopro.repository;

import java.util.Optional;

import org.marcos.autonomopro.model.db.ProductoDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends JpaRepository<ProductoDb, Long>{
    Optional<ProductoDb> findByCodigo(Long codigo);
}
