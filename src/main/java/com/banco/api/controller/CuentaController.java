// CuentaController.java
package com.banco.api.controller;  
  
import com.banco.api.dto.request.CuentaRequestDTO;  
import com.banco.api.dto.response.CuentaResponseDTO;  
import com.banco.api.service.CuentaService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  
  
import jakarta.validation.Valid;
import java.util.List;  
  
@RestController  
@RequestMapping("/api/cuentas")  
public class CuentaController {  
  
    @Autowired  
    private CuentaService cuentaService;  
  
    @PostMapping  
    public ResponseEntity<CuentaResponseDTO> crearCuenta(  
            @Valid @RequestBody CuentaRequestDTO request) {  
        CuentaResponseDTO response = cuentaService.crearCuenta(request);  
        return ResponseEntity.status(HttpStatus.CREATED).body(response);  
    }  
  
    @GetMapping  
    public ResponseEntity<List<CuentaResponseDTO>> listarCuentas(  
            @RequestParam(required = false) Long clienteId) {  
        List<CuentaResponseDTO> response = cuentaService.listarCuentas(clienteId);  
        return ResponseEntity.ok(response);  
    }  
  
    @GetMapping("/{numeroCuenta}")  
    public ResponseEntity<CuentaResponseDTO> obtenerCuenta(@PathVariable String numeroCuenta) {  
        CuentaResponseDTO response = cuentaService.obtenerCuentaPorNumero(numeroCuenta);  
        return ResponseEntity.ok(response);  
    }  
  
    @PutMapping("/{id}")  
    public ResponseEntity<CuentaResponseDTO> actualizarCuenta(  
            @PathVariable Long id,  
            @Valid @RequestBody CuentaRequestDTO request) {  
        CuentaResponseDTO response = cuentaService.actualizarCuenta(id, request);  
        return ResponseEntity.ok(response);  
    }  
  
    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {  
        cuentaService.eliminarCuenta(id);  
        return ResponseEntity.noContent().build();  
    }  
}

