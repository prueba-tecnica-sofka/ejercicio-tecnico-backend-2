package com.banco.api.controller;

import com.banco.api.dto.request.MovimientoRequestDTO;
import com.banco.api.dto.response.MovimientoResponseDTO;
import com.banco.api.entity.TipoMovimiento;
import com.banco.api.exception.GlobalExceptionHandler;
import com.banco.api.exception.ResourceNotFoundException;
import com.banco.api.exception.SaldoInsuficienteException;
import com.banco.api.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovimientoControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

        @Mock
    private MovimientoService movimientoService;

        @InjectMocks
        private MovimientoController movimientoController;

        @BeforeEach
        void setUp() {
                objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                mockMvc = MockMvcBuilders.standaloneSetup(movimientoController)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                                .build();
        }

    @Test
    @DisplayName("Debe registrar un deposito exitosamente")
    void testRegistrarDeposito() throws Exception {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        request.setValor(new BigDecimal("600.00"));

        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setFecha(LocalDateTime.now());
        response.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        response.setValor(new BigDecimal("600.00"));
        response.setSaldo(new BigDecimal("2600.00"));
        response.setNumeroCuenta("478758");

        when(movimientoService.registrarMovimiento(any())).thenReturn(response);

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.saldo").value(2600.00))
                .andExpect(jsonPath("$.numeroCuenta").value("478758"));
    }

    @Test
    @DisplayName("Debe registrar un retiro exitosamente")
    void testRegistrarRetiro() throws Exception {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.RETIRO);
        request.setValor(new BigDecimal("575.00"));

        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setFecha(LocalDateTime.now());
        response.setTipoMovimiento(TipoMovimiento.RETIRO);
        response.setValor(new BigDecimal("-575.00"));
        response.setSaldo(new BigDecimal("1425.00"));
        response.setNumeroCuenta("478758");

        when(movimientoService.registrarMovimiento(any())).thenReturn(response);

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.saldo").value(1425.00))
                .andExpect(jsonPath("$.valor").value(-575.00));
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el saldo es insuficiente")
    void testRetiroConSaldoInsuficiente() throws Exception {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("478758");
        request.setTipoMovimiento(TipoMovimiento.RETIRO);
        request.setValor(new BigDecimal("5000.00"));

        when(movimientoService.registrarMovimiento(any()))
                .thenThrow(new SaldoInsuficienteException("Saldo no disponible para realizar el retiro"));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Saldo no disponible"));
    }

    @Test
    @DisplayName("Debe listar todos los movimientos")
    void testListarMovimientos() throws Exception {
        MovimientoResponseDTO response1 = new MovimientoResponseDTO();
        response1.setId(1L);
        response1.setNumeroCuenta("478758");
        response1.setValor(new BigDecimal("600.00"));

        MovimientoResponseDTO response2 = new MovimientoResponseDTO();
        response2.setId(2L);
        response2.setNumeroCuenta("478758");
        response2.setValor(new BigDecimal("-575.00"));

        when(movimientoService.listarMovimientos(null))
                .thenReturn(Arrays.asList(response1, response2));

        mockMvc.perform(get("/api/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Debe listar movimientos por cuentaId")
    void testListarMovimientosPorCuenta() throws Exception {
        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setNumeroCuenta("478758");
        response.setValor(new BigDecimal("600.00"));

        when(movimientoService.listarMovimientos(1L))
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/movimientos?cuentaId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Debe obtener un movimiento por ID")
    void testObtenerMovimiento() throws Exception {
        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(1L);
        response.setNumeroCuenta("478758");
        response.setValor(new BigDecimal("600.00"));
        response.setSaldo(new BigDecimal("2600.00"));

        when(movimientoService.obtenerMovimiento(1L)).thenReturn(response);

        mockMvc.perform(get("/api/movimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroCuenta").value("478758"))
                .andExpect(jsonPath("$.saldo").value(2600.00));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el movimiento no existe")
    void testObtenerMovimientoNoExistente() throws Exception {
        when(movimientoService.obtenerMovimiento(999L))
                .thenThrow(new ResourceNotFoundException("Movimiento no encontrado con ID: 999"));

        mockMvc.perform(get("/api/movimientos/999"))
                .andExpect(status().isNotFound());
    }

        @Test
        @DisplayName("Debe retornar 405 con metodos soportados cuando el metodo HTTP no aplica")
        void testMetodoHttpNoPermitido() throws Exception {
                mockMvc.perform(put("/api/movimientos"))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Metodos soportados")));
        }
}