package com.banco.api.repository;

import com.banco.api.entity.Movimiento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);

    @Query("SELECT m FROM Movimiento m " +
	    "WHERE m.cuenta.cliente.clienteId = :clienteId " +
	    "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
	    "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteAndFechas(
	    @Param("clienteId") Long clienteId,
	    @Param("fechaInicio") LocalDateTime fechaInicio,
	    @Param("fechaFin") LocalDateTime fechaFin
    );
}
