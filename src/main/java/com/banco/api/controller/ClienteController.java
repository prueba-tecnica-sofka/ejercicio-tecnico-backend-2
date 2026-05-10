// ClienteController.java
package com.banco.api.controller;  
  
import com.banco.api.dto.request.ClienteRequestDTO;  
import com.banco.api.dto.response.ClienteResponseDTO;  
import com.banco.api.service.ClienteService;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  
  
import jakarta.validation.Valid;
import java.util.List;  
  
@RestController  
@RequestMapping("/api/clientes")  
public class ClienteController {  
  
    @Autowired  
    private ClienteService clienteService;  
  
    @PostMapping  
    public ResponseEntity<ClienteResponseDTO> crearCliente(  
            @Valid @RequestBody ClienteRequestDTO request) {  
        ClienteResponseDTO response = clienteService.crearCliente(request);  
        return ResponseEntity.status(HttpStatus.CREATED).body(response);  
    }  
  
    @GetMapping  
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {  
        List<ClienteResponseDTO> response = clienteService.listarClientes();  
        return ResponseEntity.ok(response);  
    }  
  
    @GetMapping("/{id}")  
    public ResponseEntity<ClienteResponseDTO> obtenerCliente(@PathVariable Long id) {  
        ClienteResponseDTO response = clienteService.obtenerCliente(id);  
        return ResponseEntity.ok(response);  
    }  
  
    @PutMapping("/{id}")  
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(  
            @PathVariable Long id,  
            @Valid @RequestBody ClienteRequestDTO request) {  
        ClienteResponseDTO response = clienteService.actualizarCliente(id, request);  
        return ResponseEntity.ok(response);  
    }  
  
    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {  
        clienteService.eliminarCliente(id);  
        return ResponseEntity.noContent().build();  
    }  
}