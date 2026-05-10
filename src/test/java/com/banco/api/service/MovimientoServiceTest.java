package com.banco.api.service;

import com.banco.api.dto.request.MovimientoRequestDTO;
import com.banco.api.dto.response.MovimientoResponseDTO;
import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Cliente;
import com.banco.api.entity.Movimiento;
import com.banco.api.entity.TipoCuenta;
import com.banco.api.entity.TipoMovimiento;
import com.banco.api.exception.ResourceNotFoundException;
import com.banco.api.exception.SaldoInsuficienteException;
import com.banco.api.mapper.MovimientoMapper;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.repository.MovimientoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

/**
 * Unit tests for MovimientoService.
 * Tests cover deposit, withdrawal, and movement listing functionality
 * with validations for insufficient balance and inactive accounts.
 */
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoMapper movimientoMapper;

    @InjectMocks
    private MovimientoService movimientoService;

    // ==================== Test Data Builders ====================

    /**
     * Creates a test cliente with basic data.
     */
    private Cliente crearCliente(Long clienteId, String nombre) {
        Cliente cliente = new Cliente();
        setField(cliente, "clienteId", clienteId);
        cliente.setNombre(nombre);
        return cliente;
    }

    /**
     * Creates a test cuenta with configurable parameters.
     */
    private Cuenta crearCuenta(Long cuentaId, String numeroCuenta, BigDecimal saldoActual,
                               boolean estado, Cliente cliente) {
        Cuenta cuenta = new Cuenta();
        setField(cuenta, "id", cuentaId);
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta(TipoCuenta.AHORROS);
        cuenta.setSaldoInicial(saldoActual);
        cuenta.setSaldoActual(saldoActual);
        cuenta.setEstado(estado);
        cuenta.setCliente(cliente);
        return cuenta;
    }

    /**
     * Creates a test movimiento with specified parameters.
     */
    private Movimiento crearMovimiento(Long id, TipoMovimiento tipo, BigDecimal valor,
                                       BigDecimal saldoResultante, Cuenta cuenta) {
        Movimiento movimiento = new Movimiento();
        setField(movimiento, "id", id);
        setField(movimiento, "fecha", LocalDateTime.now());
        movimiento.setTipoMovimiento(tipo);
        movimiento.setValor(valor);
        movimiento.setSaldo(saldoResultante);
        movimiento.setCuenta(cuenta);
        return movimiento;
    }

    /**
     * Creates a test MovimientoRequestDTO with specified parameters.
     */
    private MovimientoRequestDTO crearMovimientoRequest(String numeroCuenta,
                                                        TipoMovimiento tipo,
                                                        BigDecimal valor) {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta(numeroCuenta);
        request.setTipoMovimiento(tipo);
        request.setValor(valor);
        return request;
    }

    /**
     * Creates a test MovimientoResponseDTO with specified parameters.
     */
    private MovimientoResponseDTO crearMovimientoResponse(Long id, BigDecimal saldo) {
        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(id);
        response.setSaldo(saldo);
        return response;
    }

    // ==================== Deposit Tests ====================

    @Test
    @DisplayName("Debe registrar un depósito exitosamente")
    void testRegistrarDeposito() {
        // Given
        BigDecimal saldoInicial = new BigDecimal("2000.00");
        BigDecimal montoDeposito = new BigDecimal("600.00");
        BigDecimal saldoEsperado = new BigDecimal("2600.00");

        Cliente cliente = crearCliente(1L, "Jose Lema");
        Cuenta cuenta = crearCuenta(1L, "478758", saldoInicial, true, cliente);

        MovimientoRequestDTO request = crearMovimientoRequest("478758", TipoMovimiento.DEPOSITO, montoDeposito);

        Movimiento movimientoGuardado = crearMovimiento(1L, TipoMovimiento.DEPOSITO, montoDeposito,
                saldoEsperado, cuenta);

        MovimientoResponseDTO responseDTO = crearMovimientoResponse(1L, saldoEsperado);

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoGuardado);
        when(movimientoMapper.toResponseDTO(movimientoGuardado)).thenReturn(responseDTO);

        // When
        MovimientoResponseDTO response = movimientoService.registrarMovimiento(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(saldoEsperado, response.getSaldo());

        verify(cuentaRepository).findByNumeroCuenta("478758");
        verify(cuentaRepository).save(argThat(c ->
                c.getSaldoActual().compareTo(saldoEsperado) == 0
        ));
        verify(movimientoRepository).save(any(Movimiento.class));
    }

    // ==================== Withdrawal Tests ====================

    @Test
    @DisplayName("Debe registrar un retiro exitosamente")
    void testRegistrarRetiro() {
        // Given
        BigDecimal saldoInicial = new BigDecimal("2000.00");
        BigDecimal montoRetiro = new BigDecimal("575.00");
        BigDecimal saldoEsperado = new BigDecimal("1425.00");

        Cliente cliente = crearCliente(1L, "Test Cliente");
        Cuenta cuenta = crearCuenta(1L, "478758", saldoInicial, true, cliente);

        MovimientoRequestDTO request = crearMovimientoRequest("478758", TipoMovimiento.RETIRO, montoRetiro);

        Movimiento movimientoGuardado = crearMovimiento(1L, TipoMovimiento.RETIRO,
                montoRetiro.negate(), saldoEsperado, cuenta);

        MovimientoResponseDTO responseDTO = crearMovimientoResponse(1L, saldoEsperado);

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoGuardado);
        when(movimientoMapper.toResponseDTO(movimientoGuardado)).thenReturn(responseDTO);

        // When
        MovimientoResponseDTO response = movimientoService.registrarMovimiento(request);

        // Then
        assertNotNull(response);
        assertEquals(saldoEsperado, response.getSaldo());

        verify(cuentaRepository).save(argThat(c ->
                c.getSaldoActual().compareTo(saldoEsperado) == 0
        ));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el saldo es insuficiente para retiro")
    void testRegistrarRetiroSaldoInsuficiente() {
        // Given
        BigDecimal saldoInsuficiente = new BigDecimal("100.00");
        BigDecimal montoRetiro = new BigDecimal("500.00");

        Cliente cliente = crearCliente(1L, "Test Cliente");
        Cuenta cuenta = crearCuenta(1L, "478758", saldoInsuficiente, true, cliente);

        MovimientoRequestDTO request = crearMovimientoRequest("478758", TipoMovimiento.RETIRO, montoRetiro);

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        // When & Then
        assertThrows(SaldoInsuficienteException.class, () ->
                movimientoService.registrarMovimiento(request)
        );

        verify(cuentaRepository).findByNumeroCuenta("478758");
        verify(cuentaRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    // ==================== Account Validation Tests ====================

    @Test
    @DisplayName("Debe lanzar excepción cuando la cuenta no existe")
    void testRegistrarMovimientoCuentaNoExiste() {
        // Given
        MovimientoRequestDTO request = crearMovimientoRequest("999999", TipoMovimiento.DEPOSITO,
                new BigDecimal("100.00"));

        when(cuentaRepository.findByNumeroCuenta("999999")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                movimientoService.registrarMovimiento(request)
        );

        verify(cuentaRepository).findByNumeroCuenta("999999");
        verify(cuentaRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cuenta está inactiva")
    void testRegistrarMovimientoCuentaInactiva() {
        // Given
        Cliente cliente = crearCliente(1L, "Test Cliente");
        Cuenta cuenta = crearCuenta(1L, "478758", new BigDecimal("2000.00"), false, cliente);

        MovimientoRequestDTO request = crearMovimientoRequest("478758", TipoMovimiento.DEPOSITO,
                new BigDecimal("100.00"));

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                movimientoService.registrarMovimiento(request)
        );

        verify(cuentaRepository).findByNumeroCuenta("478758");
        verify(cuentaRepository, never()).save(any());
    }

    // ==================== Listing Tests ====================

    @Test
    @DisplayName("Debe listar todos los movimientos")
    void testListarMovimientos() {
        // Given
        Movimiento movimiento1 = crearMovimiento(1L, TipoMovimiento.DEPOSITO,
                new BigDecimal("100.00"), new BigDecimal("2100.00"), null);
        Movimiento movimiento2 = crearMovimiento(2L, TipoMovimiento.RETIRO,
                new BigDecimal("-50.00"), new BigDecimal("2050.00"), null);

        MovimientoResponseDTO dto1 = crearMovimientoResponse(1L, new BigDecimal("2100.00"));
        MovimientoResponseDTO dto2 = crearMovimientoResponse(2L, new BigDecimal("2050.00"));

        when(movimientoRepository.findAll()).thenReturn(Arrays.asList(movimiento1, movimiento2));
        when(movimientoMapper.toResponseDTO(movimiento1)).thenReturn(dto1);
        when(movimientoMapper.toResponseDTO(movimiento2)).thenReturn(dto2);

        // When
        List<MovimientoResponseDTO> result = movimientoService.listarMovimientos(null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(movimientoRepository).findAll();
    }

    @Test
    @DisplayName("Debe listar movimientos por cuentaId")
    void testListarMovimientosPorCuenta() {
        // Given
        Movimiento movimiento = crearMovimiento(1L, TipoMovimiento.DEPOSITO,
                new BigDecimal("100.00"), new BigDecimal("2100.00"), null);

        MovimientoResponseDTO dto = crearMovimientoResponse(1L, new BigDecimal("2100.00"));

        when(movimientoRepository.findByCuentaId(1L)).thenReturn(Arrays.asList(movimiento));
        when(movimientoMapper.toResponseDTO(movimiento)).thenReturn(dto);

        // When
        List<MovimientoResponseDTO> result = movimientoService.listarMovimientos(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(movimientoRepository).findByCuentaId(1L);
        verify(movimientoRepository, never()).findAll();
    }

    // ==================== Get Movement Tests ====================

    @Test
    @DisplayName("Debe obtener movimiento por ID")
    void testObtenerMovimiento() {
        // Given
        Movimiento movimiento = crearMovimiento(1L, TipoMovimiento.DEPOSITO,
                new BigDecimal("100.00"), new BigDecimal("2100.00"), null);

        MovimientoResponseDTO dto = crearMovimientoResponse(1L, new BigDecimal("2100.00"));

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        when(movimientoMapper.toResponseDTO(movimiento)).thenReturn(dto);

        // When
        MovimientoResponseDTO result = movimientoService.obtenerMovimiento(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(movimientoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el movimiento no existe")
    void testObtenerMovimientoNoExistente() {
        // Given
        when(movimientoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                movimientoService.obtenerMovimiento(999L)
        );

        verify(movimientoRepository).findById(999L);
    }
}
