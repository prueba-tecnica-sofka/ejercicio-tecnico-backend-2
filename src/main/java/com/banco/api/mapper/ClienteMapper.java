// ClienteMapper.java
package com.banco.api.mapper;  
  
import com.banco.api.dto.request.ClienteRequestDTO;  
import com.banco.api.dto.response.ClienteResponseDTO;  
import com.banco.api.entity.Cliente;  
import org.springframework.stereotype.Component;  
  
@Component  
public class ClienteMapper {  
      
    public Cliente toEntity(ClienteRequestDTO dto) {  
        if (dto == null) {  
            return null;  
        }  
          
        Cliente cliente = new Cliente();  
        cliente.setNombre(dto.getNombre());  
        cliente.setGenero(dto.getGenero());  
        cliente.setEdad(dto.getEdad());  
        cliente.setIdentificacion(dto.getIdentificacion());  
        cliente.setDireccion(dto.getDireccion());  
        cliente.setTelefono(dto.getTelefono());  
        cliente.setContrasena(dto.getContraseña());  
        cliente.setEstado(dto.getEstado());  
        return cliente;  
    }  
      
    public ClienteResponseDTO toResponseDTO(Cliente entity) {  
        if (entity == null) {  
            return null;  
        }  
          
        ClienteResponseDTO dto = new ClienteResponseDTO();  
        dto.setClienteId(entity.getClienteId());  
        dto.setNombre(entity.getNombre());  
        dto.setGenero(entity.getGenero());  
        dto.setEdad(entity.getEdad());  
        dto.setIdentificacion(entity.getIdentificacion());  
        dto.setDireccion(entity.getDireccion());  
        dto.setTelefono(entity.getTelefono());  
        dto.setEstado(entity.getEstado());  
        dto.setCreatedAt(entity.getCreatedAt());  
        return dto;  
    }  
      
    public void updateEntityFromDTO(ClienteRequestDTO dto, Cliente entity) {  
        if (dto == null || entity == null) {  
            return;  
        }  
          
        entity.setNombre(dto.getNombre());  
        entity.setGenero(dto.getGenero());  
        entity.setEdad(dto.getEdad());  
        entity.setIdentificacion(dto.getIdentificacion());  
        entity.setDireccion(dto.getDireccion());  
        entity.setTelefono(dto.getTelefono());  
        if (dto.getContraseña() != null) {  
            entity.setContrasena(dto.getContraseña());  
        }  
        entity.setEstado(dto.getEstado());  
    }  
}