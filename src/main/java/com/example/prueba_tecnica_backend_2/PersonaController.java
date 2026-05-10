package com.example.prueba_tecnica_backend_2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    private final List<Persona> personas = new ArrayList<>();

    public PersonaController() {
        personas.add(new Persona(new Date(), "Jose Lema", "478758", "Ahorros", 2000, "true", "", 2000));
        personas.add(new Persona(new Date(), "Marianela Montalvo", "225487", "Corriente", 100, "true", "", 100));
        personas.add(new Persona(new Date(), "Juan Osorio", "495878", "Ahorros", 0, "true", "", 0));
        personas.add(new Persona(new Date(), "Marianela Montalvo", "496825", "Ahorros", 540, "true", "", 540));
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listarPersonas() {
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/saludo")
    public ResponseEntity<String> saludoPersona() {
        return ResponseEntity.ok("Hello, Persona!");
    }
}
