package com.banco.api.service;  
  
import com.banco.api.dto.request.ClienteRequestDTO;  
import com.banco.api.dto.response.ClienteResponseDTO;  
import com.banco.api.entity.Cliente;  
import com.banco.api.exception.ResourceNotFoundException;
import com.banco.api.mapper.ClienteMapper;  
import com.banco.api.repository.ClienteRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  
  
import java.util.List;  
import java.util.stream.Collectors;  
  
@Service  
public class ClienteService {  
  
    @Autowired  
    private ClienteRepository clienteRepository;  
  
    @Autowired  
    private ClienteMapper clienteMapper;  
  
    @Transactional  
    public ClienteResponseDTO crearCliente(ClienteRequestDTO request) {  
        Cliente cliente = clienteMapper.toEntity(request);  
        Cliente saved = clienteRepository.save(cliente);  
        return clienteMapper.toResponseDTO(saved);  
    }  
  
    public List<ClienteResponseDTO> listarClientes() {  
        return clienteRepository.findAll()  
                .stream()  
                .map(clienteMapper::toResponseDTO)  
                .collect(Collectors.toList());  
    }  
  
    public ClienteResponseDTO obtenerCliente(Long id) {  
        Cliente cliente = clienteRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));  
        return clienteMapper.toResponseDTO(cliente);  
    }  
  
    @Transactional  
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request) {  
        Cliente cliente = clienteRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));  
        clienteMapper.updateEntityFromDTO(request, cliente);  
        Cliente updated = clienteRepository.save(cliente);  
        return clienteMapper.toResponseDTO(updated);  
    }  
  
    @Transactional  
    public void eliminarCliente(Long id) {  
        Cliente cliente = clienteRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));  
        clienteRepository.delete(cliente);  
    }  
}