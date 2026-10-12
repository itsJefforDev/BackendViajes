package com.gestionViajes.Viajes.businessLayer;

import com.gestionViajes.businessLayer.dto.ClienteDTO;
import com.gestionViajes.businessLayer.service.impl.ClienteServiceImpl;
import com.gestionViajes.persistenceLayer.entity.Cliente;
import com.gestionViajes.persistenceLayer.mapper.ClienteMapper;
import com.gestionViajes.persistenceLayer.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para ClienteServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (ClienteRepository y ClienteMapper)
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Unit Tests")
public class ClienteServiceTest {

    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private ClienteServiceImpl clienteService;

    // DATOS DE PRUEBA
    private Cliente validCliente;
    private ClienteDTO validClienteDTO;
    private Long validClienteId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validClienteId = 1L;

        // Entidad Cliente para usar en tests
        validCliente = new Cliente();
        validCliente.setId(validClienteId);
        validCliente.setNombre("Juan Pérez");
        validCliente.setEmail("juan.perez@email.com");
        validCliente.setTelefono("123456789");
        validCliente.setDireccion("Calle 123");

        // DTO Cliente para usar en tests
        validClienteDTO = new ClienteDTO();
        validClienteDTO.setId(validClienteId);
        validClienteDTO.setNombre("Juan Pérez");
        validClienteDTO.setEmail("juan.perez@email.com");
        validClienteDTO.setTelefono("123456789");
        validClienteDTO.setDireccion("Calle 123");
    }

    // ==================== READ TODOS TESTS ====================

    @Test
    @DisplayName("READ - Debe retornar lista de clientes cuando existen registros")
    void READTodos_ShouldReturnClienteList() {
        // ARRANGE (Given)
        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombre("Maria Gomez");

        ClienteDTO clienteDTO2 = new ClienteDTO();
        clienteDTO2.setId(2L);
        clienteDTO2.setNombre("Maria Gomez");

        List<Cliente> clienteList = Arrays.asList(validCliente, cliente2);

        when(clienteRepository.findAll()).thenReturn(clienteList);
        when(clienteMapper.toDto(validCliente)).thenReturn(validClienteDTO);
        when(clienteMapper.toDto(cliente2)).thenReturn(clienteDTO2);

        // ACT (When)
        List<ClienteDTO> result = clienteService.listarTodos();

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("nombre")
                .containsExactly("Juan Pérez", "Maria Gomez");

        verify(clienteRepository, times(1)).findAll();
        verify(clienteMapper, times(2)).toDto(any(Cliente.class));
    }

    @Test
    @DisplayName("READ - Debe retornar lista vacía si no hay clientes")
    void READTodos_EmptyRepository_ShouldReturnEmptyList() {
        // ARRANGE
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<ClienteDTO> result = clienteService.listarTodos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(clienteRepository, times(1)).findAll();
        verify(clienteMapper, never()).toDto(any(Cliente.class));
    }

    // ==================== OBTENER POR ID TESTS ====================

    @Test
    @DisplayName("OBTENER POR ID - Cliente existente debe retornar DTO correspondiente")
    void obtenerPorId_ExistingId_ShouldReturnClienteDTO() {
        // ARRANGE
        when(clienteRepository.findById(validClienteId))
                .thenReturn(Optional.of(validCliente));
        when(clienteMapper.toDto(validCliente))
                .thenReturn(validClienteDTO);

        // ACT
        ClienteDTO result = clienteService.obtenerPorId(validClienteId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validClienteId);
        assertThat(result.getNombre()).isEqualTo("Juan Pérez");
        assertThat(result.getEmail()).isEqualTo("juan.perez@email.com");

        verify(clienteRepository, times(1)).findById(validClienteId);
        verify(clienteMapper, times(1)).toDto(validCliente);
    }

    @Test
    @DisplayName("OBTENER POR ID - Cliente inexistente debe lanzar EntityNotFoundException")
    void obtenerPorId_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(clienteRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> clienteService.obtenerPorId(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con ID: " + nonExistentId);

        verify(clienteRepository, times(1)).findById(nonExistentId);
        verify(clienteMapper, never()).toDto(any(Cliente.class));
    }

    // ==================== UPDATE CLIENTE TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos debe mapear, guardar y retornar el cliente creado")
    void UPDATE_ValidData_ShouldReturnCreatedClienteDTO() {
        // ARRANGE
        ClienteDTO inputDTO = new ClienteDTO();
        inputDTO.setNombre("Juan Pérez");
        inputDTO.setEmail("juan.perez@email.com");
        inputDTO.setTelefono("123456789");
        inputDTO.setDireccion("Calle 123");

        when(clienteMapper.toEntity(inputDTO)).thenReturn(validCliente);
        when(clienteRepository.save(validCliente)).thenReturn(validCliente);
        when(clienteMapper.toDto(validCliente)).thenReturn(validClienteDTO);

        // ACT
        ClienteDTO result = clienteService.crear(inputDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validClienteId);
        assertThat(result.getNombre()).isEqualTo("Juan Pérez");

        verify(clienteMapper, times(1)).toEntity(inputDTO);
        verify(clienteRepository, times(1)).save(validCliente);
        verify(clienteMapper, times(1)).toDto(validCliente);
    }

    // ==================== UPDATE CLIENTE TESTS ====================

    @Test
    @DisplayName("UPDATE - Cliente existente debe modificar atributos y retornar actualizado")
    void UPDATE_ExistingCliente_ShouldReturnUpdatedClienteDTO() {
        // ARRANGE
        ClienteDTO updateDTO = new ClienteDTO();
        updateDTO.setNombre("Juan Actualizado");
        updateDTO.setEmail("juan.nuevo@email.com");
        updateDTO.setTelefono("987654321");
        updateDTO.setDireccion("Avenida Siempre Viva");

        ClienteDTO updatedResultDTO = new ClienteDTO();
        updatedResultDTO.setId(validClienteId);
        updatedResultDTO.setNombre("Juan Actualizado");
        updatedResultDTO.setEmail("juan.nuevo@email.com");
        updatedResultDTO.setTelefono("987654321");
        updatedResultDTO.setDireccion("Avenida Siempre Viva");

        when(clienteRepository.findById(validClienteId))
                .thenReturn(Optional.of(validCliente));
        when(clienteRepository.save(validCliente))
                .thenReturn(validCliente);
        when(clienteMapper.toDto(validCliente))
                .thenReturn(updatedResultDTO);

        // ACT
        ClienteDTO result = clienteService.actualizar(validClienteId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validClienteId);
        assertThat(result.getNombre()).isEqualTo("Juan Actualizado");
        assertThat(result.getEmail()).isEqualTo("juan.nuevo@email.com");

        verify(clienteRepository, times(1)).findById(validClienteId);
        verify(clienteRepository, times(1)).save(validCliente);
        verify(clienteMapper, times(1)).toDto(validCliente);
    }

    @Test
    @DisplayName("UPDATE - Cliente inexistente debe lanzar EntityNotFoundException")
    void UPDATE_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(clienteRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> clienteService.actualizar(nonExistentId, validClienteDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con ID: " + nonExistentId);

        verify(clienteRepository, times(1)).findById(nonExistentId);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // ==================== DELETE CLIENTE TESTS ====================

    @Test
    @DisplayName("DELETE - Cliente existente debe eliminar correctamente")
    void DELETE_ExistingId_ShouldCompleteSuccessfully() {
        // ARRANGE
        when(clienteRepository.existsById(validClienteId)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(validClienteId);

        // ACT & ASSERT
        assertThatCode(() -> clienteService.eliminar(validClienteId))
                .doesNotThrowAnyException();

        verify(clienteRepository, times(1)).existsById(validClienteId);
        verify(clienteRepository, times(1)).deleteById(validClienteId);
    }

    @Test
    @DisplayName("DELETE - Cliente inexistente debe lanzar EntityNotFoundException")
    void DELETE_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(clienteRepository.existsById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> clienteService.eliminar(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con ID: " + nonExistentId);

        verify(clienteRepository, times(1)).existsById(nonExistentId);
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}