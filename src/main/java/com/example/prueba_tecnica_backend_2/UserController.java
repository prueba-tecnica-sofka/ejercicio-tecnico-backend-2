package com.example.prueba_tecnica_backend_2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {
    private List<User> users = new ArrayList<>();

    public UserController() {
        users.add(new User(new Date(), "Jose Lema", "478758", "Ahorros", 2000, "true", "", 2000));
        users.add(new User(new Date(), "Marianela Montalvo", "225487", "Corriente", 100, "true", "", 100));
        users.add(new User(new Date(), "Juan Osorio", "495878", "Ahorros", 0, "true", "", 0));
        users.add(new User(new Date(), "Marianela Montalvo", "496825", "Ahorros", 540, "true", "", 540));
    }

    @GetMapping("/clientes")
    public List<User> getUsers() {
        return users;
    }

    @GetMapping("/")
    public String getUser() {
        return "Hello, User!";
    }
}
