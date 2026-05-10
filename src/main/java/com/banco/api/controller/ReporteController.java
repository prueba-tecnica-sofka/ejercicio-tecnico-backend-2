// ReporteController.java
package com.banco.api.controller;  
  
import com.banco.api.dto.response.ReporteMovimientoDTO;  
import com.banco.api.service.ReporteService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.format.annotation.DateTimeFormat;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  
  
import java.time.LocalDate;  
import java.util.List;  
  
@RestController  
@RequestMapping("/api/reportes")  
public class ReporteController {  
  
    @Autowired  
    private ReporteService reporteService;  
  
    @GetMapping  
    public ResponseEntity<List<ReporteMovimientoDTO>> generarReporte(  
            @RequestParam Long clienteId,  
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,  
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {  
        List<ReporteMovimientoDTO> response = reporteService.generarReporte(clienteId, fechaInicio, fechaFin);  
        return ResponseEntity.ok(response);  
    }  
}