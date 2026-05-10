// MovimientoController.java
package com.banco.api.controller;  
  
import com.banco.api.dto.request.MovimientoRequestDTO;  
import com.banco.api.dto.response.MovimientoResponseDTO;  
import com.banco.api.service.MovimientoService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  
  
import jakarta.validation.Valid;
import java.util.List;  
  
@RestController  
@RequestMapping("/api/movimientos")  
public class MovimientoController {  
  
    @Autowired  
    private MovimientoService movimientoService;  
  
    @PostMapping  
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(  
            @Valid @RequestBody MovimientoRequestDTO request) {  
        MovimientoResponseDTO response = movimientoService.registrarMovimiento(request);  
        return ResponseEntity.status(HttpStatus.CREATED).body(response);  
    }  
  
    @GetMapping  
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientos(  
            @RequestParam(required = false) Long cuentaId) {  
        List<MovimientoResponseDTO> response = movimientoService.listarMovimientos(cuentaId);  
        return ResponseEntity.ok(response);  
    }  
  
    @GetMapping("/{id}")  
    public ResponseEntity<MovimientoResponseDTO> obtenerMovimiento(@PathVariable Long id) {  
        MovimientoResponseDTO response = movimientoService.obtenerMovimiento(id);  
        return ResponseEntity.ok(response);  
    }  
}