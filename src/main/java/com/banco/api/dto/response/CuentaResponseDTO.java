package com.banco.api.dto.response;

import com.banco.api.entity.TipoCuenta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaResponseDTO {
	private Long id;
	private String numeroCuenta;
	private TipoCuenta tipoCuenta;
	private BigDecimal saldoInicial;
	private BigDecimal saldoActual;
	private Boolean estado;
	private ClienteInfo cliente;

	@Data
	public static class ClienteInfo {
		private Long clienteId;
		private String nombre;
	}
}
