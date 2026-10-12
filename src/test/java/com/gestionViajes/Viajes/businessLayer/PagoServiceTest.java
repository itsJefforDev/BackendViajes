package com.gestionViajes.Viajes.businessLayer;

import com.gestionViajes.businessLayer.dto.PagoDTO;
import com.gestionViajes.businessLayer.service.impl.PagoServiceImpl;
import com.gestionViajes.persistenceLayer.entity.Pago;
import com.gestionViajes.persistenceLayer.mapper.PagoMapper;
import com.gestionViajes.persistenceLayer.repository.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para PagoServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (PagoRepository y PagoMapper)
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Unit Tests")
public class PagoServiceTest {

    // DEPENDENCIAS MOCKEADAS
    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PagoMapper pagoMapper;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private PagoServiceImpl pagoService;

    // DATOS DE PRUEBA
    private Pago validPago;
    private PagoDTO validPagoDTO;
    private Long validPagoId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables con información propia de Pagos
     */
    @BeforeEach
    void setUp() {
        validPagoId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Entidad Pago para usar en tests
        validPago = new Pago();
        validPago.setId(validPagoId);
        validPago.setMetodoDePago("TARJETA_CREDITO");
        validPago.setMonto(BigDecimal.valueOf(450000.00));
        validPago.setFecha(now);
        validPago.setEstado("APROBADO");
        validPago.setNumeroTransaccion("TXN-12345678");

        // DTO Pago para usar en tests
        validPagoDTO = new PagoDTO();
        validPagoDTO.setId(validPagoId);
        validPagoDTO.setMetodoDePago("TARJETA_CREDITO");
        validPagoDTO.setMonto(BigDecimal.valueOf(450000.00));
        validPagoDTO.setFecha(now);
        validPagoDTO.setEstado("APROBADO");
        validPagoDTO.setNumeroTransaccion("TXN-12345678");
    }

    // ==================== LISTAR TODOS TESTS ====================

    @Test
    @DisplayName("READ - Debe retornar lista de Pagos cuando existen registros")
    void listarTodos_ShouldReturnPagoList() {
        // ARRANGE (Given)
        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setMetodoDePago("PSE");
        pago2.setMonto(BigDecimal.valueOf(120000.00));
        pago2.setEstado("PENDIENTE");

        PagoDTO pagoDTO2 = new PagoDTO();
        pagoDTO2.setId(2L);
        pagoDTO2.setMetodoDePago("PSE");
        pagoDTO2.setMonto(BigDecimal.valueOf(120000.00));
        pagoDTO2.setEstado("PENDIENTE");

        List<Pago> pagoList = Arrays.asList(validPago, pago2);

        when(pagoRepository.findAll()).thenReturn(pagoList);
        when(pagoMapper.toDto(validPago)).thenReturn(validPagoDTO);
        when(pagoMapper.toDto(pago2)).thenReturn(pagoDTO2);

        // ACT (When)
        List<PagoDTO> result = pagoService.listarTodos();

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("metodoDePago")
                .containsExactly("TARJETA_CREDITO", "PSE");

        verify(pagoRepository, times(1)).findAll();
        verify(pagoMapper, times(2)).toDto(any(Pago.class));
    }

    @Test
    @DisplayName("READ - Debe retornar lista vacía si no hay Pagos")
    void listarTodos_EmptyRepository_ShouldReturnEmptyList() {
        // ARRANGE
        when(pagoRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<PagoDTO> result = pagoService.listarTodos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(pagoRepository, times(1)).findAll();
        verify(pagoMapper, never()).toDto(any(Pago.class));
    }

    // ==================== OBTENER POR ID TESTS ====================

    @Test
    @DisplayName("OBTENER POR ID - Pago existente debe retornar DTO correspondiente")
    void obtenerPorId_ExistingId_ShouldReturnPagoDTO() {
        // ARRANGE
        when(pagoRepository.findById(validPagoId))
                .thenReturn(Optional.of(validPago));
        when(pagoMapper.toDto(validPago))
                .thenReturn(validPagoDTO);

        // ACT
        PagoDTO result = pagoService.obtenerPorId(validPagoId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validPagoId);
        assertThat(result.getMetodoDePago()).isEqualTo("TARJETA_CREDITO");
        assertThat(result.getMonto()).isEqualByComparingTo(BigDecimal.valueOf(450000.00));
        assertThat(result.getEstado()).isEqualTo("APROBADO");

        verify(pagoRepository, times(1)).findById(validPagoId);
        verify(pagoMapper, times(1)).toDto(validPago);
    }

    @Test
    @DisplayName("OBTENER POR ID - Pago inexistente debe lanzar EntityNotFoundException")
    void obtenerPorId_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(pagoRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> pagoService.obtenerPorId(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pago no encontrado con ID: " + nonExistentId);

        verify(pagoRepository, times(1)).findById(nonExistentId);
        verify(pagoMapper, never()).toDto(any(Pago.class));
    }

    // ==================== CREAR PAGO TESTS ====================

    @Test
    @DisplayName("CREAR - Datos válidos debe mapear, guardar y retornar el Pago creado")
    void crear_ValidData_ShouldReturnCreatedPagoDTO() {
        // ARRANGE
        PagoDTO inputDTO = new PagoDTO();
        inputDTO.setMetodoDePago("EFECTIVO");
        inputDTO.setMonto(BigDecimal.valueOf(200000.00));
        inputDTO.setEstado("PENDIENTE");
        inputDTO.setNumeroTransaccion("TXN-99887766");

        when(pagoMapper.toEntity(inputDTO)).thenReturn(validPago);
        when(pagoRepository.save(validPago)).thenReturn(validPago);
        when(pagoMapper.toDto(validPago)).thenReturn(validPagoDTO);

        // ACT
        PagoDTO result = pagoService.crear(inputDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validPagoId);
        assertThat(result.getMetodoDePago()).isEqualTo("TARJETA_CREDITO");

        verify(pagoMapper, times(1)).toEntity(inputDTO);
        verify(pagoRepository, times(1)).save(validPago);
        verify(pagoMapper, times(1)).toDto(validPago);
    }

    // ==================== ACTUALIZAR PAGO TESTS ====================

    @Test
    @DisplayName("UPDATE - Pago existente debe modificar atributos y retornar actualizado")
    void actualizar_ExistingPago_ShouldReturnUpdatedPagoDTO() {
        // ARRANGE
        PagoDTO updateDTO = new PagoDTO();
        updateDTO.setMetodoDePago("TRANSFERENCIA");
        updateDTO.setMonto(BigDecimal.valueOf(500000.00));
        updateDTO.setEstado("APROBADO");
        updateDTO.setNumeroTransaccion("TXN-NEW-001");

        PagoDTO updatedResultDTO = new PagoDTO();
        updatedResultDTO.setId(validPagoId);
        updatedResultDTO.setMetodoDePago("TRANSFERENCIA");
        updatedResultDTO.setMonto(BigDecimal.valueOf(500000.00));
        updatedResultDTO.setEstado("APROBADO");
        updatedResultDTO.setNumeroTransaccion("TXN-NEW-001");

        when(pagoRepository.findById(validPagoId))
                .thenReturn(Optional.of(validPago));
        when(pagoRepository.save(validPago))
                .thenReturn(validPago);
        when(pagoMapper.toDto(validPago))
                .thenReturn(updatedResultDTO);

        // ACT
        PagoDTO result = pagoService.actualizar(validPagoId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validPagoId);
        assertThat(result.getMetodoDePago()).isEqualTo("TRANSFERENCIA");
        assertThat(result.getMonto()).isEqualByComparingTo(BigDecimal.valueOf(500000.00));
        assertThat(result.getEstado()).isEqualTo("APROBADO");

        verify(pagoRepository, times(1)).findById(validPagoId);
        verify(pagoRepository, times(1)).save(validPago);
        verify(pagoMapper, times(1)).toDto(validPago);
    }

    @Test
    @DisplayName("UPDATE - Pago inexistente debe lanzar EntityNotFoundException")
    void actualizar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(pagoRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> pagoService.actualizar(nonExistentId, validPagoDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pago no encontrado con ID: " + nonExistentId);

        verify(pagoRepository, times(1)).findById(nonExistentId);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    // ==================== ELIMINAR PAGO TESTS ====================

    @Test
    @DisplayName("DELETE - Pago existente debe eliminar correctamente")
    void eliminar_ExistingId_ShouldCompleteSuccessfully() {
        // ARRANGE
        when(pagoRepository.existsById(validPagoId)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(validPagoId);

        // ACT & ASSERT
        assertThatCode(() -> pagoService.eliminar(validPagoId))
                .doesNotThrowAnyException();

        verify(pagoRepository, times(1)).existsById(validPagoId);
        verify(pagoRepository, times(1)).deleteById(validPagoId);
    }

    @Test
    @DisplayName("DELETE - Pago inexistente debe lanzar EntityNotFoundException")
    void eliminar_NonExistentId_ShouldThrowEntityNotFoundException() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(pagoRepository.existsById(nonExistentId)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> pagoService.eliminar(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pago no encontrado con ID: " + nonExistentId);

        verify(pagoRepository, times(1)).existsById(nonExistentId);
        verify(pagoRepository, never()).deleteById(anyLong());
    }
}