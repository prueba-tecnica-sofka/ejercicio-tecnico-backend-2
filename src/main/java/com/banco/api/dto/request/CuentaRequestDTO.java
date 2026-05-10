
package com.banco.api.dto.request;

import com.banco.api.entity.TipoCuenta;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data  
public class CuentaRequestDTO {  
    @NotBlank(message = "El número de cuenta es obligatorio")  
    private String numeroCuenta;  
      
    @NotNull(message = "El tipo de cuenta es obligatorio")  
    private TipoCuenta tipoCuenta;  
      
    @NotNull(message = "El saldo inicial es obligatorio")  
    @DecimalMin(value = "0.00", message = "El saldo inicial debe ser mayor o igual a 0")  
    private BigDecimal saldoInicial;  
      
    private Boolean estado = true;  
      
    @NotNull(message = "El clienteId es obligatorio")  
    private Long clienteId;  
}