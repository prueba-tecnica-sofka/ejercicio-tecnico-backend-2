package com.banco.api.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReporteMovimientoDTO {
	private LocalDate fecha;
	private String cliente;
	private String numeroCuenta;
	private String tipo;
	private BigDecimal saldoInicial;
	private Boolean estado;
	private BigDecimal movimiento;
	private BigDecimal saldoDisponible;
}
