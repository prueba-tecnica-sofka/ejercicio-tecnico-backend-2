package com.banco.api.repository;

import com.banco.api.entity.Cuenta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

	List<Cuenta> findByClienteClienteId(Long clienteId);

	@Query("SELECT c FROM Cuenta c WHERE c.cliente.clienteId = :clienteId AND c.estado = true")
	List<Cuenta> findCuentasActivasByCliente(@Param("clienteId") Long clienteId);
}
