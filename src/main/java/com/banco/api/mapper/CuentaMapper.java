// CuentaMapper.java
package com.banco.api.mapper;  
  
import com.banco.api.dto.request.CuentaRequestDTO;  
import com.banco.api.dto.response.CuentaResponseDTO;  
import com.banco.api.entity.Cuenta;  
import com.banco.api.entity.Cliente;  
import org.springframework.stereotype.Component;  
  
@Component  
public class CuentaMapper {  
      
    public Cuenta toEntity(CuentaRequestDTO dto, Cliente cliente) {  
        if (dto == null) {  
            return null;  
        }  
          
        Cuenta cuenta = new Cuenta();  
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());  
        cuenta.setTipoCuenta(dto.getTipoCuenta());  
        cuenta.setSaldoInicial(dto.getSaldoInicial());  
        cuenta.setSaldoActual(dto.getSaldoInicial());  
        cuenta.setEstado(dto.getEstado());  
        cuenta.setCliente(cliente);  
        return cuenta;  
    }  
      
    public CuentaResponseDTO toResponseDTO(Cuenta entity) {  
        if (entity == null) {  
            return null;  
        }  
          
        CuentaResponseDTO dto = new CuentaResponseDTO();  
        dto.setId(entity.getId());  
        dto.setNumeroCuenta(entity.getNumeroCuenta());  
        dto.setTipoCuenta(entity.getTipoCuenta());  
        dto.setSaldoInicial(entity.getSaldoInicial());  
        dto.setSaldoActual(entity.getSaldoActual());  
        dto.setEstado(entity.getEstado());  
          
        if (entity.getCliente() != null) {  
            CuentaResponseDTO.ClienteInfo clienteInfo = new CuentaResponseDTO.ClienteInfo();  
            clienteInfo.setClienteId(entity.getCliente().getClienteId());  
            clienteInfo.setNombre(entity.getCliente().getNombre());  
            dto.setCliente(clienteInfo);  
        }  
          
        return dto;  
    }  
}