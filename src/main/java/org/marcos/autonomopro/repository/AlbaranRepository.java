package org.marcos.autonomopro.repository;

import java.util.Optional;

import org.marcos.autonomopro.model.db.AlbaranDb;
import org.marcos.autonomopro.model.db.FacturaDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbaranRepository extends JpaRepository<AlbaranDb, String>{
    AlbaranDb findByNumeroAlbaran(String numeroAlbaran);

}
