// MovimientoMapper.java
package com.banco.api.mapper;  
  
import com.banco.api.dto.response.MovimientoResponseDTO;  
import com.banco.api.entity.Movimiento;  
import org.springframework.stereotype.Component;  
  
@Component  
public class MovimientoMapper {  
      
    public MovimientoResponseDTO toResponseDTO(Movimiento entity) {  
        if (entity == null) {  
            return null;  
        }  
          
        MovimientoResponseDTO dto = new MovimientoResponseDTO();  
        dto.setId(entity.getId());  
        dto.setFecha(entity.getFecha());  
        dto.setTipoMovimiento(entity.getTipoMovimiento());  
        dto.setValor(entity.getValor());  
        dto.setSaldo(entity.getSaldo());  
          
        if (entity.getCuenta() != null) {  
            dto.setNumeroCuenta(entity.getCuenta().getNumeroCuenta());  
        }  
          
        return dto;  
    }  
}