package com.banco.api.dto.response;

import com.banco.api.entity.TipoMovimiento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoResponseDTO {
	private Long id;
	private LocalDateTime fecha;
	private TipoMovimiento tipoMovimiento;
	private BigDecimal valor;
	private BigDecimal saldo;
	private String numeroCuenta;
}
