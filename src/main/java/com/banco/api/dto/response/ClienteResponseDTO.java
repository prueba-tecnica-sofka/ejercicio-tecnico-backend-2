package com.banco.api.dto.response;

import com.banco.api.entity.Genero;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteResponseDTO {
	private Long clienteId;
	private String nombre;
	private Genero genero;
	private Integer edad;
	private String identificacion;
	private String direccion;
	private String telefono;
	private Boolean estado;
	private LocalDateTime createdAt;
}
