package com.banco.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.api.entity.Cliente;
import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Genero;
import com.banco.api.entity.TipoCuenta;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTest.JpaAuditingTestConfig.class)
@ContextConfiguration(classes = RepositoryTest.RepositoryJpaTestApplication.class)
@EntityScan(basePackages = "com.banco.api.entity")
class RepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Test
    void testFindClienteById() {
        Cliente cliente = buildCliente("1728394051");
        Cliente saved = clienteRepository.save(cliente);

        assertThat(clienteRepository.findByClienteId(saved.getClienteId()))
                .isPresent()
                .get()
                .extracting(Cliente::getNombre)
                .isEqualTo("Maria Perez");
    }

    @Test
    void testFindCuentaByNumero() {
        Cliente cliente = clienteRepository.save(buildCliente("1728394052"));

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta(TipoCuenta.AHORROS);
        cuenta.setSaldoInicial(new BigDecimal("250.00"));
        cuenta.setSaldoActual(new BigDecimal("250.00"));
        cuenta.setEstado(Boolean.TRUE);
        cuenta.setCliente(cliente);
        cuentaRepository.save(cuenta);

        assertThat(cuentaRepository.findByNumeroCuenta("478758"))
                .isPresent()
                .get()
                .extracting(Cuenta::getNumeroCuenta)
                .isEqualTo("478758");

        assertThat(cuentaRepository.findByClienteClienteId(cliente.getClienteId()))
                .hasSize(1)
                .extracting(Cuenta::getNumeroCuenta)
                .containsExactly("478758");

        assertThat(movimientoRepository.findByCuentaId(cuenta.getId())).isEmpty();
    }

    private Cliente buildCliente(String identificacion) {
        Cliente cliente = new Cliente();
        cliente.setNombre("Maria Perez");
        cliente.setGenero(Genero.FEMENINO);
        cliente.setEdad(31);
        cliente.setIdentificacion(identificacion);
        cliente.setDireccion("Quito");
        cliente.setTelefono("0999999999");
        cliente.setContrasena("secure-pass");
        cliente.setEstado(Boolean.TRUE);
        return cliente;
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class JpaAuditingTestConfig {
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EnableJpaRepositories(basePackages = "com.banco.api.repository")
    static class RepositoryJpaTestApplication {
    }
}
