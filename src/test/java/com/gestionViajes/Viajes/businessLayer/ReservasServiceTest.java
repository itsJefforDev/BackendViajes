package com.gestionViajes.Viajes.businessLayer;

import com.gestionViajes.businessLayer.dto.ReservasDTO;
import com.gestionViajes.businessLayer.service.impl.ReservasServiceImpl;
import com.gestionViajes.persistenceLayer.entity.Reservas;
import com.gestionViajes.persistenceLayer.repository.ReservasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para ReservasServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (ReservasRepository)
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservasService - Unit Tests")
public class ReservasServiceTest {

    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ReservasRepository reservasRepository;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private ReservasServiceImpl reservasService;

    // DATOS DE PRUEBA
    private Reservas validReserva;
    private ReservasDTO validReservasDTO;
    private Long validReservaId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables con información propia de Reservas
     */
    @BeforeEach
    void setUp() {
        validReservaId = 1L;
        LocalDateTime fechaTest = LocalDateTime.of(2026, 6, 15,5,10);

        // Entidad Reservas para usar en tests
        validReserva = new Reservas();
        validReserva.setId(validReservaId);
        validReserva.setFechaDeReserva(fechaTest);
        validReserva.setEstado("CONFIRMADA");

        // DTO Reservas para usar en tests
        validReservasDTO = new ReservasDTO();
        validReservasDTO.setId(validReservaId);
        validReservasDTO.setFechaDeReserva(fechaTest);
        validReservasDTO.setEstado("CONFIRMADA");
    }

    // ==================== LISTAR TODAS TESTS ====================

    @Test
    @DisplayName("READ - Debe retornar lista de Reservas cuando existen registros")
    void listarTodas_ShouldReturnReservasList() {
        // ARRANGE (Given)
        Reservas reserva2 = new Reservas();
        reserva2.setId(2L);
        reserva2.setFechaDeReserva(LocalDateTime.of(2026, 7, 20,10,5));
        reserva2.setEstado("PENDIENTE");

        List<Reservas> reservasLista = Arrays.asList(validReserva, reserva2);

        when(reservasRepository.findAll()).thenReturn(reservasLista);

        // ACT (When)
        List<ReservasDTO> result = reservasService.listarTodas();

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("estado")
                .containsExactly("CONFIRMADA", "PENDIENTE");

        verify(reservasRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("READ - Debe retornar lista vacía si no hay Reservas")
    void listarTodas_EmptyRepository_ShouldReturnEmptyList() {
        // ARRANGE
        when(reservasRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<ReservasDTO> result = reservasService.listarTodas();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(reservasRepository, times(1)).findAll();
    }

    // ==================== OBTENER POR ID TESTS ====================

    @Test
    @DisplayName("OBTENER POR ID - Reserva existente debe retornar DTO correspondiente")
    void obtenerPorId_ExistingId_ShouldReturnReservasDTO() {
        // ARRANGE
        when(reservasRepository.findById(validReservaId))
                .thenReturn(Optional.of(validReserva));

        // ACT
        ReservasDTO result = reservasService.obtenerPorId(validReservaId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validReservaId);
        assertThat(result.getEstado()).isEqualTo("CONFIRMADA");
        assertThat(result.getFechaDeReserva()).isEqualTo(validReserva.getFechaDeReserva());

        verify(reservasRepository, times(1)).findById(validReservaId);
    }

    @Test
    @DisplayName("OBTENER POR ID - Reserva inexistente debe lanzar EntityNotFoundException")
    void obtenerPorId_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(reservasRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> reservasService.obtenerPorId(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Reserva no encontrada con ID: " + nonExistentId);

        verify(reservasRepository, times(1)).findById(nonExistentId);
    }

    // ==================== CREAR RESERVA TESTS ====================

    @Test
    @DisplayName("CREAR - Datos válidos debe guardar y retornar la Reserva creada")
    void crear_ValidData_ShouldReturnCreatedReservasDTO() {
        // ARRANGE
        ReservasDTO inputDTO = new ReservasDTO();
        inputDTO.setFechaDeReserva(LocalDateTime.of(2026, 8, 10,5,6));
        inputDTO.setEstado("PENDIENTE");

        when(reservasRepository.save(any(Reservas.class))).thenAnswer(invocation -> {
            Reservas r = invocation.getArgument(0);
            r.setId(validReservaId); // Simula la asignación de ID al guardar
            return r;
        });

        // ACT
        ReservasDTO result = reservasService.crear(inputDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validReservaId);
        assertThat(result.getEstado()).isEqualTo("PENDIENTE");
        assertThat(result.getFechaDeReserva()).isEqualTo(inputDTO.getFechaDeReserva());

        verify(reservasRepository, times(1)).save(any(Reservas.class));
    }

    // ==================== ACTUALIZAR RESERVA TESTS ====================

    @Test
    @DisplayName("UPDATE - Reserva existente debe modificar atributos y retornar actualizada")
    void actualizar_ExistingReserva_ShouldReturnUpdatedReservasDTO() {
        // ARRANGE
        ReservasDTO updateDTO = new ReservasDTO();
        updateDTO.setFechaDeReserva(LocalDateTime.of(2026, 9, 1,10,5));
        updateDTO.setEstado("COMPLETADA");

        when(reservasRepository.findById(validReservaId))
                .thenReturn(Optional.of(validReserva));
        when(reservasRepository.save(any(Reservas.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        ReservasDTO result = reservasService.actualizar(validReservaId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validReservaId);
        assertThat(result.getEstado()).isEqualTo("COMPLETADA");
        assertThat(result.getFechaDeReserva()).isEqualTo(LocalDateTime.of(2026, 9, 1,10,5));

        verify(reservasRepository, times(1)).findById(validReservaId);
        verify(reservasRepository, times(1)).save(any(Reservas.class));
    }

    @Test
    @DisplayName("UPDATE - Reserva inexistente debe lanzar EntityNotFoundException")
    void actualizar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(reservasRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> reservasService.actualizar(nonExistentId, validReservasDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Reserva no encontrada con ID: " + nonExistentId);

        verify(reservasRepository, times(1)).findById(nonExistentId);
        verify(reservasRepository, never()).save(any(Reservas.class));
    }

    // ==================== ELIMINAR RESERVA TESTS ====================

    @Test
    @DisplayName("DELETE - Reserva existente debe eliminar correctamente")
    void eliminar_ExistingId_ShouldCompleteSuccessfully() {
        // ARRANGE
        when(reservasRepository.existsById(validReservaId)).thenReturn(true);
        doNothing().when(reservasRepository).deleteById(validReservaId);

        // ACT & ASSERT
        assertThatCode(() -> reservasService.eliminar(validReservaId))
                .doesNotThrowAnyException();

        verify(reservasRepository, times(1)).existsById(validReservaId);
        verify(reservasRepository, times(1)).deleteById(validReservaId);
    }

    @Test
    @DisplayName("DELETE - Reserva inexistente debe lanzar EntityNotFoundException")
    void eliminar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(reservasRepository.existsById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> reservasService.eliminar(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Reserva no encontrada con ID: " + nonExistentId);

        verify(reservasRepository, times(1)).existsById(nonExistentId);
        verify(reservasRepository, never()).deleteById(anyLong());
    }
}