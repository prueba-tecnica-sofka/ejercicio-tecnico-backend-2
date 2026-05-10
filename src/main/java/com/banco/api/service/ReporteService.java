package com.banco.api.service;  
  
import com.banco.api.dto.response.ReporteMovimientoDTO;  
import com.banco.api.entity.Movimiento;  
import com.banco.api.repository.MovimientoRepository;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
  
import java.time.LocalDate;  
import java.time.LocalDateTime;  
import java.util.List;  
import java.util.stream.Collectors;  
  
@Service  
public class ReporteService {  
  
    @Autowired  
    private MovimientoRepository movimientoRepository;  
  
    public List<ReporteMovimientoDTO> generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {  
        LocalDateTime inicio = fechaInicio.atStartOfDay();  
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);  
  
        List<Movimiento> movimientos = movimientoRepository.findByClienteAndFechas(clienteId, inicio, fin);  
  
        return movimientos.stream()  
                .map(m -> {  
                    ReporteMovimientoDTO dto = new ReporteMovimientoDTO();  
                    dto.setFecha(m.getFecha().toLocalDate());  
                    dto.setCliente(m.getCuenta().getCliente().getNombre());  
                    dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());  
                    dto.setTipo(m.getCuenta().getTipoCuenta().name());  
                    dto.setSaldoInicial(m.getCuenta().getSaldoInicial());  
                    dto.setEstado(m.getCuenta().getEstado());  
                    dto.setMovimiento(m.getValor());  
                    dto.setSaldoDisponible(m.getSaldo());  
                    return dto;  
                })  
                .collect(Collectors.toList());  
    }  
}