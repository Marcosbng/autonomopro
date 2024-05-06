package org.marcos.autonomopro.repository;

import org.marcos.autonomopro.model.db.ClienteDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<ClienteDb, Long>{
    
    

}
