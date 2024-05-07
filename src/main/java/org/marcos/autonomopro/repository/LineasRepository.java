package org.marcos.autonomopro.repository;

import java.util.List;

import org.marcos.autonomopro.model.db.LineasDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineasRepository extends JpaRepository<LineasDb, Long>{
    List<LineasDb> findByFacturaNumeroFactura(String numeroFactura);

    boolean existsByProductoCodigo(Long codigoProducto);
}
