package com.banco.api.service;

import com.banco.api.dto.request.ClienteRequestDTO;
import com.banco.api.dto.response.ClienteResponseDTO;
import com.banco.api.entity.Cliente;
import com.banco.api.entity.Genero;
import com.banco.api.exception.DuplicateResourceException;
import com.banco.api.mapper.ClienteMapper;
import com.banco.api.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("Debe crear un cliente cuando la identificacion no existe")
    void testCrearCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNombre("Jose Lema");
        request.setGenero(Genero.MASCULINO);
        request.setEdad(35);
        request.setIdentificacion("1234567890");
        request.setDireccion("Otavalo sn y principal");
        request.setTelefono("098254785");
        request.setContraseña("1234");
        request.setEstado(Boolean.TRUE);

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setContrasena("1234");
        cliente.setEstado(Boolean.TRUE);

        Cliente saved = new Cliente();
        ReflectionTestUtils.setField(saved, "clienteId", 4L);
        saved.setNombre(request.getNombre());
        saved.setGenero(request.getGenero());
        saved.setEdad(request.getEdad());
        saved.setIdentificacion(request.getIdentificacion());
        saved.setDireccion(request.getDireccion());
        saved.setTelefono(request.getTelefono());
        saved.setContrasena("1234");
        saved.setEstado(Boolean.TRUE);

        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setClienteId(4L);
        response.setNombre(request.getNombre());
        response.setIdentificacion(request.getIdentificacion());
        response.setEstado(Boolean.TRUE);

        when(clienteRepository.existsByIdentificacion(request.getIdentificacion())).thenReturn(false);
        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(saved);
        when(clienteMapper.toResponseDTO(saved)).thenReturn(response);

        ClienteResponseDTO result = clienteService.crearCliente(request);

        assertNotNull(result);
        assertEquals(4L, result.getClienteId());
        assertEquals("1234567890", result.getIdentificacion());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Debe rechazar la creacion cuando la identificacion ya existe")
    void testCrearClienteDuplicado() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setIdentificacion("1234567890");

        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> clienteService.crearCliente(request));

        verify(clienteMapper, never()).toEntity(any());
        verify(clienteRepository, never()).save(any());
    }
}
