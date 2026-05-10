package com.banco.api.service;  
  
import com.banco.api.dto.request.MovimientoRequestDTO;  
import com.banco.api.dto.response.MovimientoResponseDTO;  
import com.banco.api.entity.Cuenta;  
import com.banco.api.entity.Movimiento;  
import com.banco.api.entity.TipoMovimiento;  
import com.banco.api.exception.ResourceNotFoundException;  
import com.banco.api.exception.SaldoInsuficienteException;  
import com.banco.api.mapper.MovimientoMapper;  
import com.banco.api.repository.CuentaRepository;  
import com.banco.api.repository.MovimientoRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  
  
import java.math.BigDecimal;  
import java.time.LocalDateTime;  
import java.util.List;  
import java.util.stream.Collectors;  
  
@Service  
public class MovimientoService {  
  
    @Autowired  
    private MovimientoRepository movimientoRepository;  
  
    @Autowired  
    private CuentaRepository cuentaRepository;  
  
    @Autowired  
    private MovimientoMapper movimientoMapper;  
  
    @Transactional  
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request) {  
        // 1. Buscar la cuenta  
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())  
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));  
  
        // 2. Validar que la cuenta esté activa  
        if (!cuenta.getEstado()) {  
            throw new ResourceNotFoundException("La cuenta está inactiva");  
        }  
  
        // 3. Calcular el valor del movimiento (negativo si es retiro)  
        BigDecimal valor = request.getTipoMovimiento() == TipoMovimiento.RETIRO  
                ? request.getValor().negate()  
                : request.getValor();  
  
        // 4. Calcular nuevo saldo  
        BigDecimal nuevoSaldo = cuenta.getSaldoActual().add(valor);  
  
        // 5. Validar saldo disponible (solo para retiros)  
        if (request.getTipoMovimiento() == TipoMovimiento.RETIRO && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {  
            throw new SaldoInsuficienteException("Saldo no disponible para realizar el retiro");  
        }  
  
        // 6. Actualizar saldo de la cuenta  
        cuenta.setSaldoActual(nuevoSaldo);  
        cuentaRepository.save(cuenta);  
  
        // 7. Crear y guardar el movimiento  
        Movimiento movimiento = new Movimiento();  
        movimiento.setFecha(LocalDateTime.now());  
        movimiento.setTipoMovimiento(request.getTipoMovimiento());  
        movimiento.setValor(valor);  
        movimiento.setSaldo(nuevoSaldo);  
        movimiento.setCuenta(cuenta);  
  
        Movimiento saved = movimientoRepository.save(movimiento);  
  
        // 8. Retornar respuesta  
        return movimientoMapper.toResponseDTO(saved);  
    }  
  
    public List<MovimientoResponseDTO> listarMovimientos(Long cuentaId) {  
        if (cuentaId != null) {  
            return movimientoRepository.findByCuentaId(cuentaId)  
                    .stream()  
                    .map(movimientoMapper::toResponseDTO)  
                    .collect(Collectors.toList());  
        }  
        return movimientoRepository.findAll()  
                .stream()  
                .map(movimientoMapper::toResponseDTO)  
                .collect(Collectors.toList());  
    }  
  
    public MovimientoResponseDTO obtenerMovimiento(Long id) {  
        Movimiento movimiento = movimientoRepository.findById(id)  
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));  
        return movimientoMapper.toResponseDTO(movimiento);  
    }  
}