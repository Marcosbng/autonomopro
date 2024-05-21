package org.marcos.autonomopro.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.marcos.autonomopro.model.db.FacturaDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasRepository extends JpaRepository<FacturaDb, String>{

    Optional<FacturaDb> findByNumeroFactura(String numeroFactura);
    
    List<FacturaDb> findByFechaVencimientoBeforeAndEstado(Date fechaActual, String estado);

    List<FacturaDb> findByClienteId(Long clienteId);

    Optional<FacturaDb> findTopByOrderByNumeroFacturaDesc();

    List<FacturaDb> findByNumeroFacturaContaining(String searchTerm);

    @Query("SELECT FUNCTION('DATE_FORMAT', f.fechaEmision, '%Y-%m-%d') as dia, COUNT(f) as cantidad " +
           "FROM FacturaDb f " +
           "GROUP BY dia " +
           "ORDER BY dia")
    List<Object[]> findCantidadFacturasGroupByDia();

    @Query("SELECT FUNCTION('DATE_FORMAT', f.fechaEmision, '%Y-%m-%d') as dia, SUM(f.importeTotal) as totalImporte " +
           "FROM FacturaDb f " +
           "GROUP BY dia " +
           "ORDER BY dia")
    List<Object[]> findImporteTotalFacturasGroupByDia();
}
