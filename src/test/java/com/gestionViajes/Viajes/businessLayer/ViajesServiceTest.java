package com.gestionViajes.Viajes.businessLayer;

import com.gestionViajes.businessLayer.dto.ViajesDTO;
import com.gestionViajes.businessLayer.service.impl.ViajesServiceImpl;
import com.gestionViajes.persistenceLayer.entity.Viajes;
import com.gestionViajes.persistenceLayer.mapper.ViajesMapper;
import com.gestionViajes.persistenceLayer.repository.ViajesRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para ViajesServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (ViajesRepository y ViajesMapper)
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ViajesService - Unit Tests")
public class ViajesServiceTest {

    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ViajesRepository viajesRepository;

    @Mock
    private ViajesMapper viajesMapper;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private ViajesServiceImpl viajesService;

    // DATOS DE PRUEBA
    private Viajes validViaje;
    private ViajesDTO validViajesDTO;
    private Long validViajeId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables con información propia de Viajes
     */
    @BeforeEach
    void setUp() {
        validViajeId = 1L;
        Date fechaTest = new Date();

        // Entidad Viajes para usar en tests
        validViaje = new Viajes();
        validViaje.setId(validViajeId);
        validViaje.setDuracion(fechaTest);
        validViaje.setPrecio(1500.00);
        validViaje.setFechas_disponibles(fechaTest);
        validViaje.setDescripcion("Viaje de prueba a la playa");

        // DTO Viajes para usar en tests
        validViajesDTO = new ViajesDTO();
        validViajesDTO.setId(validViajeId);
        validViajesDTO.setDuracion(fechaTest);
        validViajesDTO.setPrecio(1500.00);
        validViajesDTO.setFechas_disponibles(fechaTest);
        validViajesDTO.setDescripcion("Viaje de prueba a la playa");
    }

    // ==================== LISTAR TODOS TESTS ====================

    @Test
    @DisplayName("READ - Debe retornar lista de Viajes cuando existen registros")
    void listarTodos_ShouldReturnViajesList() {
        // ARRANGE (Given)
        Viajes viaje2 = new Viajes();
        viaje2.setId(2L);
        viaje2.setPrecio(2300.00);
        viaje2.setDescripcion("Viaje de prueba a la montaña");

        ViajesDTO viajesDTO2 = new ViajesDTO();
        viajesDTO2.setId(2L);
        viajesDTO2.setPrecio(2300.00);
        viajesDTO2.setDescripcion("Viaje de prueba a la montaña");

        List<Viajes> viajesLista = Arrays.asList(validViaje, viaje2);

        when(viajesRepository.findAll()).thenReturn(viajesLista);
        when(viajesMapper.toDto(validViaje)).thenReturn(validViajesDTO);
        when(viajesMapper.toDto(viaje2)).thenReturn(viajesDTO2);

        // ACT (When)
        List<ViajesDTO> result = viajesService.listarTodos();

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("descripcion")
                .containsExactly("Viaje de prueba a la playa", "Viaje de prueba a la montaña");

        verify(viajesRepository, times(1)).findAll();
        verify(viajesMapper, times(2)).toDto(any(Viajes.class));
    }

    @Test
    @DisplayName("READ - Debe retornar lista vacía si no hay Viajes")
    void listarTodos_EmptyRepository_ShouldReturnEmptyList() {
        // ARRANGE
        when(viajesRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<ViajesDTO> result = viajesService.listarTodos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(viajesRepository, times(1)).findAll();
        verify(viajesMapper, never()).toDto(any(Viajes.class));
    }

    // ==================== OBTENER POR ID TESTS ====================

    @Test
    @DisplayName("OBTENER POR ID - Viaje existente debe retornar DTO correspondiente")
    void obtenerPorId_ExistingId_ShouldReturnViajesDTO() {
        // ARRANGE
        when(viajesRepository.findById(validViajeId))
                .thenReturn(Optional.of(validViaje));
        when(viajesMapper.toDto(validViaje))
                .thenReturn(validViajesDTO);

        // ACT
        ViajesDTO result = viajesService.obtenerPorId(validViajeId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validViajeId);
        assertThat(result.getPrecio()).isEqualTo(1500.00);
        assertThat(result.getDescripcion()).isEqualTo("Viaje de prueba a la playa");

        verify(viajesRepository, times(1)).findById(validViajeId);
        verify(viajesMapper, times(1)).toDto(validViaje);
    }

    @Test
    @DisplayName("OBTENER POR ID - Viaje inexistente debe lanzar EntityNotFoundException")
    void obtenerPorId_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(viajesRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> viajesService.obtenerPorId(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Viaje no encontrado con ID: " + nonExistentId);

        verify(viajesRepository, times(1)).findById(nonExistentId);
        verify(viajesMapper, never()).toDto(any(Viajes.class));
    }

    // ==================== CREAR VIAJE TESTS ====================

    @Test
    @DisplayName("CREAR - Datos válidos debe mapear, guardar y retornar el Viaje creado")
    void crear_ValidData_ShouldReturnCreatedViajesDTO() {
        // ARRANGE
        ViajesDTO inputDTO = new ViajesDTO();
        inputDTO.setPrecio(1800.00);
        inputDTO.setDescripcion("Nuevo viaje a la ciudad");

        when(viajesMapper.toEntity(inputDTO)).thenReturn(validViaje);
        when(viajesRepository.save(validViaje)).thenReturn(validViaje);
        when(viajesMapper.toDto(validViaje)).thenReturn(validViajesDTO);

        // ACT
        ViajesDTO result = viajesService.crear(inputDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validViajeId);
        assertThat(result.getPrecio()).isEqualTo(1500.00);

        verify(viajesMapper, times(1)).toEntity(inputDTO);
        verify(viajesRepository, times(1)).save(validViaje);
        verify(viajesMapper, times(1)).toDto(validViaje);
    }

    // ==================== ACTUALIZAR VIAJE TESTS ====================

    @Test
    @DisplayName("UPDATE - Viaje existente debe modificar atributos y retornar actualizado")
    void actualizar_ExistingViaje_ShouldReturnUpdatedViajesDTO() {
        // ARRANGE
        ViajesDTO updateDTO = new ViajesDTO();
        updateDTO.setPrecio(2000.00);
        updateDTO.setDescripcion("Viaje actualizado");

        ViajesDTO updatedResultDTO = new ViajesDTO();
        updatedResultDTO.setId(validViajeId);
        updatedResultDTO.setPrecio(2000.00);
        updatedResultDTO.setDescripcion("Viaje actualizado");

        when(viajesRepository.findById(validViajeId))
                .thenReturn(Optional.of(validViaje));
        when(viajesRepository.save(validViaje))
                .thenReturn(validViaje);
        when(viajesMapper.toDto(validViaje))
                .thenReturn(updatedResultDTO);

        // ACT
        ViajesDTO result = viajesService.actualizar(validViajeId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validViajeId);
        assertThat(result.getPrecio()).isEqualTo(2000.00);
        assertThat(result.getDescripcion()).isEqualTo("Viaje actualizado");

        verify(viajesRepository, times(1)).findById(validViajeId);
        verify(viajesRepository, times(1)).save(validViaje);
        verify(viajesMapper, times(1)).toDto(validViaje);
    }

    @Test
    @DisplayName("UPDATE - Viaje inexistente debe lanzar EntityNotFoundException")
    void actualizar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(viajesRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> viajesService.actualizar(nonExistentId, validViajesDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Viaje no encontrado con ID: " + nonExistentId);

        verify(viajesRepository, times(1)).findById(nonExistentId);
        verify(viajesRepository, never()).save(any(Viajes.class));
    }

    // ==================== ELIMINAR VIAJE TESTS ====================

    @Test
    @DisplayName("DELETE - Viaje existente debe eliminar correctamente")
    void eliminar_ExistingId_ShouldCompleteSuccessfully() {
        // ARRANGE
        when(viajesRepository.existsById(validViajeId)).thenReturn(true);
        doNothing().when(viajesRepository).deleteById(validViajeId);

        // ACT & ASSERT
        assertThatCode(() -> viajesService.eliminar(validViajeId))
                .doesNotThrowAnyException();

        verify(viajesRepository, times(1)).existsById(validViajeId);
        verify(viajesRepository, times(1)).deleteById(validViajeId);
    }

    @Test
    @DisplayName("DELETE - Viaje inexistente debe lanzar EntityNotFoundException")
    void eliminar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(viajesRepository.existsById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> viajesService.eliminar(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Viaje no encontrado con ID: " + nonExistentId);

        verify(viajesRepository, times(1)).existsById(nonExistentId);
        verify(viajesRepository, never()).deleteById(anyLong());
    }
}