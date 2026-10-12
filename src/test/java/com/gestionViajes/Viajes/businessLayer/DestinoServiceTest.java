package com.gestionViajes.Viajes.businessLayer;

import com.gestionViajes.businessLayer.dto.DestinoDTO;
import com.gestionViajes.businessLayer.service.impl.DestinoServiceImpl;
import com.gestionViajes.persistenceLayer.entity.Destino;
import com.gestionViajes.persistenceLayer.mapper.DestinoMapper;
import com.gestionViajes.persistenceLayer.repository.DestinoRepository;
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
 * Unit Tests para DestinoServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (DestinoRepository y DestinoMapper)
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DestinoService - Unit Tests")
public class DestinoServiceTest {

    // DEPENDENCIAS MOCKEADAS
    @Mock
    private DestinoRepository destinoRepository;

    @Mock
    private DestinoMapper destinoMapper;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private DestinoServiceImpl destinoService;

    // DATOS DE PRUEBA
    private Destino validDestino;
    private DestinoDTO validDestinoDTO;
    private Long validDestinoId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables con información propia de Destinos
     */
    @BeforeEach
    void setUp() {
        validDestinoId = 1L;

        // Entidad Destino para usar en tests
        validDestino = new Destino();
        validDestino.setId(validDestinoId);
        validDestino.setNombre("Bogotá");
        validDestino.setDescripcion("Capital de Colombia");
        validDestino.setAtraccionesTuristicas("Monserrate, Candelaria");
        validDestino.setClima("Frío");
        validDestino.setRecomendaciones("Llevar chaqueta y paraguas");

        // DTO Destino para usar en tests
        validDestinoDTO = new DestinoDTO();
        validDestinoDTO.setId(validDestinoId);
        validDestinoDTO.setNombre("Bogotá");
        validDestinoDTO.setDescripcion("Capital de Colombia");
        validDestinoDTO.setAtraccionesTuristicas("Monserrate, Candelaria");
        validDestinoDTO.setClima("Frío");
        validDestinoDTO.setRecomendaciones("Llevar chaqueta y paraguas");
    }

    // ==================== LISTAR TODOS TESTS ====================

    @Test
    @DisplayName("READ - Debe retornar lista de Destinos cuando existen registros")
    void listarTodos_ShouldReturnDestinoList() {
        // ARRANGE (Given)
        Destino destino2 = new Destino();
        destino2.setId(2L);
        destino2.setNombre("Cartagena");
        destino2.setClima("Cálido");

        DestinoDTO destinoDTO2 = new DestinoDTO();
        destinoDTO2.setId(2L);
        destinoDTO2.setNombre("Cartagena");
        destinoDTO2.setClima("Cálido");

        List<Destino> destinoList = Arrays.asList(validDestino, destino2);

        when(destinoRepository.findAll()).thenReturn(destinoList);
        when(destinoMapper.toDto(validDestino)).thenReturn(validDestinoDTO);
        when(destinoMapper.toDto(destino2)).thenReturn(destinoDTO2);

        // ACT (When)
        List<DestinoDTO> result = destinoService.listarTodos();

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("nombre")
                .containsExactly("Bogotá", "Cartagena");

        verify(destinoRepository, times(1)).findAll();
        verify(destinoMapper, times(2)).toDto(any(Destino.class));
    }

    @Test
    @DisplayName("READ - Debe retornar lista vacía si no hay Destinos")
    void listarTodos_EmptyRepository_ShouldReturnEmptyList() {
        // ARRANGE
        when(destinoRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<DestinoDTO> result = destinoService.listarTodos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(destinoRepository, times(1)).findAll();
        verify(destinoMapper, never()).toDto(any(Destino.class));
    }

    // ==================== OBTENER POR ID TESTS ====================

    @Test
    @DisplayName("OBTENER POR ID - Destino existente debe retornar DTO correspondiente")
    void obtenerPorId_ExistingId_ShouldReturnDestinoDTO() {
        // ARRANGE
        when(destinoRepository.findById(validDestinoId))
                .thenReturn(Optional.of(validDestino));
        when(destinoMapper.toDto(validDestino))
                .thenReturn(validDestinoDTO);

        // ACT
        DestinoDTO result = destinoService.obtenerPorId(validDestinoId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validDestinoId);
        assertThat(result.getNombre()).isEqualTo("Bogotá");
        assertThat(result.getClima()).isEqualTo("Frío");

        verify(destinoRepository, times(1)).findById(validDestinoId);
        verify(destinoMapper, times(1)).toDto(validDestino);
    }

    @Test
    @DisplayName("OBTENER POR ID - Destino inexistente debe lanzar EntityNotFoundException")
    void obtenerPorId_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(destinoRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> destinoService.obtenerPorId(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Destino no encontrado con ID: " + nonExistentId);

        verify(destinoRepository, times(1)).findById(nonExistentId);
        verify(destinoMapper, never()).toDto(any(Destino.class));
    }

    // ==================== CREAR DESTINO TESTS ====================

    @Test
    @DisplayName("CREAR - Datos válidos debe mapear, guardar y retornar el Destino creado")
    void crear_ValidData_ShouldReturnCreatedDestinoDTO() {
        // ARRANGE
        DestinoDTO inputDTO = new DestinoDTO();
        inputDTO.setNombre("Medellín");
        inputDTO.setDescripcion("Ciudad de la eterna primavera");
        inputDTO.setAtraccionesTuristicas("Parque Explora, Metrocable");
        inputDTO.setClima("Templado");

        when(destinoMapper.toEntity(inputDTO)).thenReturn(validDestino);
        when(destinoRepository.save(validDestino)).thenReturn(validDestino);
        when(destinoMapper.toDto(validDestino)).thenReturn(validDestinoDTO);

        // ACT
        DestinoDTO result = destinoService.crear(inputDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validDestinoId);
        assertThat(result.getNombre()).isEqualTo("Bogotá");

        verify(destinoMapper, times(1)).toEntity(inputDTO);
        verify(destinoRepository, times(1)).save(validDestino);
        verify(destinoMapper, times(1)).toDto(validDestino);
    }

    // ==================== ACTUALIZAR DESTINO TESTS ====================

    @Test
    @DisplayName("UPDATE - Destino existente debe modificar atributos y retornar actualizado")
    void actualizar_ExistingDestino_ShouldReturnUpdatedDestinoDTO() {
        // ARRANGE
        DestinoDTO updateDTO = new DestinoDTO();
        updateDTO.setNombre("Bogotá Actualizado");
        updateDTO.setDescripcion("Nueva descripción");
        updateDTO.setAtraccionesTuristicas("Nuevas atracciones");
        updateDTO.setClima("Lluvioso");
        updateDTO.setRecomendaciones("Nuevas recomendaciones");

        DestinoDTO updatedResultDTO = new DestinoDTO();
        updatedResultDTO.setId(validDestinoId);
        updatedResultDTO.setNombre("Bogotá Actualizado");
        updatedResultDTO.setDescripcion("Nueva descripción");
        updatedResultDTO.setAtraccionesTuristicas("Nuevas atracciones");
        updatedResultDTO.setClima("Lluvioso");
        updatedResultDTO.setRecomendaciones("Nuevas recomendaciones");

        when(destinoRepository.findById(validDestinoId))
                .thenReturn(Optional.of(validDestino));
        when(destinoRepository.save(validDestino))
                .thenReturn(validDestino);
        when(destinoMapper.toDto(validDestino))
                .thenReturn(updatedResultDTO);

        // ACT
        DestinoDTO result = destinoService.actualizar(validDestinoId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validDestinoId);
        assertThat(result.getNombre()).isEqualTo("Bogotá Actualizado");
        assertThat(result.getClima()).isEqualTo("Lluvioso");

        verify(destinoRepository, times(1)).findById(validDestinoId);
        verify(destinoRepository, times(1)).save(validDestino);
        verify(destinoMapper, times(1)).toDto(validDestino);
    }

    @Test
    @DisplayName("UPDATE - Destino inexistente debe lanzar EntityNotFoundException")
    void actualizar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(destinoRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> destinoService.actualizar(nonExistentId, validDestinoDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Destino no encontrado con ID: " + nonExistentId);

        verify(destinoRepository, times(1)).findById(nonExistentId);
        verify(destinoRepository, never()).save(any(Destino.class));
    }

    // ==================== ELIMINAR DESTINO TESTS ====================

    @Test
    @DisplayName("DELETE - Destino existente debe eliminar correctamente")
    void eliminar_ExistingId_ShouldCompleteSuccessfully() {
        // ARRANGE
        when(destinoRepository.existsById(validDestinoId)).thenReturn(true);
        doNothing().when(destinoRepository).deleteById(validDestinoId);

        // ACT & ASSERT
        assertThatCode(() -> destinoService.eliminar(validDestinoId))
                .doesNotThrowAnyException();

        verify(destinoRepository, times(1)).existsById(validDestinoId);
        verify(destinoRepository, times(1)).deleteById(validDestinoId);
    }

    @Test
    @DisplayName("DELETE - Destino inexistente debe lanzar EntityNotFoundException")
    void eliminar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(destinoRepository.existsById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> destinoService.eliminar(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Destino no encontrado con ID: " + nonExistentId);

        verify(destinoRepository, times(1)).existsById(nonExistentId);
        verify(destinoRepository, never()).deleteById(anyLong());
    }
}