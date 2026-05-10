## Generación de la API

### Primeras pruebas de la API

1. Descargar la plantilla de proyecto de Java cdel portal https://start.spring.io/
  
2. Copiar la plantilla e insertarla al proyecto, con las versiones recomendadas por la página

3. Ejecutar el proyecto para verficar que funciona con la opción del IDE `Run Java`

4. Crea una nueva clase de Java para la clase `Usuario`

```java
package com.example.prueba_tecnica_backend_2;


public class UserController {

}
```

 - Añadir el decorador `@RestController`

 - Instalar la dependencia en el archivo `POM.xml` o en el archivo `build.gradle`

```java
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

5. Crea el método de prueba de la API

```java
package com.example.prueba_tecnica_backend_2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/")
    public String getUser() {
        return "Hello, User!";
    }
}
```

- Ve en postman que el método efectivamente funciona

6. en el archivo `application.properties` personaliza el puerto

```java
spring.application.name=prueba-tecnica-backend-2
server.port=3000
```

probar el llamado a la API en el nuevo puerto

7. Crear una lista de usuarios y mostrarla

- Crear la clase `Usuarios`

```java
package com.example.prueba_tecnica_backend_2;

import java.util.Date;

public class User {
    private Date fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private double saldoInicial;
    private String estado;
    private String movimiento;
    private double saldoDisponible;
}
```

- Dentro de la clase `Usuarios`, genera el constructor

```java
package com.example.prueba_tecnica_backend_2;

import java.util.Date;

public class User {
    private Date fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private double saldoInicial;
    private String estado;
    private String movimiento;
    private double saldoDisponible;

    // Constructor
    public User(Date fecha, String cliente, String numeroCuenta, String tipo, double saldoInicial, String estado, String movimiento, double saldoDisponible) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }
}
```

- En el controlador `UserController` crea la lista de usuarios

```java
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
```

- a la clase `Users` agrega los correspondientes getters y setters

```java
package com.example.prueba_tecnica_backend_2;

import java.util.Date;

public class User {
    private Date fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private double saldoInicial;
    private String estado;
    private String movimiento;
    private double saldoDisponible;

    public User() {
    }

    public User(Date fecha, String cliente, String numeroCuenta, String tipo, double saldoInicial, String estado, String movimiento, double saldoDisponible) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}
```

- En el controlador `UserController` implementa el llamado de la lista de usuarios

```java
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

    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    @GetMapping("/")
    public String getUser() {
        return "Hello, User!";
    }
}
```