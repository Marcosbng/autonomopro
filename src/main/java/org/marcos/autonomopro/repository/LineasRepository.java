package org.marcos.autonomopro.repository;

import java.util.List;

import org.marcos.autonomopro.model.db.LineasDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineasRepository extends JpaRepository<LineasDb, Long>{
    List<LineasDb> findByFacturaNumeroFactura(String numeroFactura);
}
