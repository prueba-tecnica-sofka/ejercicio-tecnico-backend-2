package com.banco.api.service;  
  
import com.banco.api.dto.request.CuentaRequestDTO;  
import com.banco.api.dto.response.CuentaResponseDTO;  
import com.banco.api.entity.Cuenta;  
import com.banco.api.entity.Cliente;  
import com.banco.api.exception.ResourceNotFoundException;  
import com.banco.api.mapper.CuentaMapper;  
import com.banco.api.repository.CuentaRepository;  
import com.banco.api.repository.ClienteRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  
  
import java.util.List;  
import java.util.stream.Collectors;  
  
@Service  
public class CuentaService {  
  
    @Autowired  
    private CuentaRepository cuentaRepository;  
  
    @Autowired  
    private ClienteRepository clienteRepository;  
  
    @Autowired  
    private CuentaMapper cuentaMapper;  
  
    @Transactional  
    public CuentaResponseDTO crearCuenta(CuentaRequestDTO request) {  
        Cliente cliente = clienteRepository.findById(request.getClienteId())  
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + request.getClienteId()));  
          
        Cuenta cuenta = cuentaMapper.toEntity(request, cliente);  
        Cuenta saved = cuentaRepository.save(cuenta);  
        return cuentaMapper.toResponseDTO(saved);  
    }  
  
    public List<CuentaResponseDTO> listarCuentas(Long clienteId) {  
        if (clienteId != null) {  
            return cuentaRepository.findByClienteClienteId(clienteId)  
                    .stream()  
                    .map(cuentaMapper::toResponseDTO)  
                    .collect(Collectors.toList());  
        }  
        return cuentaRepository.findAll()  
                .stream()  
                .map(cuentaMapper::toResponseDTO)  
                .collect(Collectors.toList());  
    }  
  
    public CuentaResponseDTO obtenerCuentaPorNumero(String numeroCuenta) {  
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)  
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));  
        return cuentaMapper.toResponseDTO(cuenta);  
    }  
  
    @Transactional  
    public CuentaResponseDTO actualizarCuenta(Long id, CuentaRequestDTO request) {  
        Cuenta cuenta = cuentaRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));  
          
        cuenta.setNumeroCuenta(request.getNumeroCuenta());  
        cuenta.setTipoCuenta(request.getTipoCuenta());  
        cuenta.setEstado(request.getEstado());  
          
        Cuenta updated = cuentaRepository.save(cuenta);  
        return cuentaMapper.toResponseDTO(updated);  
    }  
  
    @Transactional  
    public void eliminarCuenta(Long id) {  
        Cuenta cuenta = cuentaRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));  
        cuentaRepository.delete(cuenta);  
    }  
}