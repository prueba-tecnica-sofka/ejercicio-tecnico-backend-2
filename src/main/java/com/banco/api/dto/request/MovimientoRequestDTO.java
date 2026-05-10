
package com.banco.api.dto.request;

import com.banco.api.entity.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data  
public class MovimientoRequestDTO {  
    @NotBlank(message = "El número de cuenta es obligatorio")  
    private String numeroCuenta;  
      
    @NotNull(message = "El tipo de movimiento es obligatorio")  
    private TipoMovimiento tipoMovimiento;  
      
    @NotNull(message = "El valor es obligatorio")  
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")  
    private BigDecimal valor;  
}