package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AssemblyService assemblyService;

    @Mock
    private AgendaMapper agendaMapper;

    @InjectMocks
    private AgendaService agendaService;

    private Assembly assembly;
    private Agenda agenda;
    private AgendaDTO agendaDTO;

    @BeforeEach
    void setup() {
        assembly = new Assembly();
        assembly.setId(1L);

        agenda = new Agenda();
        agenda.setId(1L);
        agenda.setDescription("Test agenda");
        agenda.setAssembly(assembly);
        agenda.setStart(LocalDateTime.now().plusMinutes(1));
        agenda.setEnd(LocalDateTime.now().plusMinutes(2));

        agendaDTO = new AgendaDTO();
        agendaDTO.setId(1L);
        agendaDTO.setDescription("Test agenda");
        agendaDTO.setAssemblyId(1L);
        agendaDTO.setStart(agenda.getStart());
        agendaDTO.setEnd(agenda.getEnd());
        agendaDTO.setState(AgendaState.IN_VOTING);
    }

    @Test
    void shouldCreateAgenda() {
        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaMapper.toEntity(agendaDTO)).thenReturn(agenda);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaDTO);

        AgendaDTO result = agendaService.create(agendaDTO);

        assertNotNull(result);
        assertEquals(agendaDTO.getDescription(), result.getDescription());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenCreateAgendaWithAssemblyNotFound() {
        when(assemblyService.findByIdEntity(1L)).thenThrow(new ResourceNotFoundException("Assembly not found with id: 1"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.create(agendaDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(assemblyService).findByIdEntity(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenCreateAgendaWithInvalidDates() {
        agendaDTO.setStart(LocalDateTime.now().minusDays(1));
        agendaDTO.setEnd(LocalDateTime.now().minusDays(2));

        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaMapper.toEntity(agendaDTO)).thenReturn(agenda);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> agendaService.create(agendaDTO));

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date or earlier than the current date."));
        verify(assemblyService).findByIdEntity(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldUpdateAgenda() {
        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaDTO);

        AgendaDTO result = agendaService.update(agendaDTO);

        assertNotNull(result);
        assertEquals(agendaDTO.getDescription(), result.getDescription());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenUpdateAssemblyNotFound() {
        when(assemblyService.findByIdEntity(1L)).thenThrow(new ResourceNotFoundException("Assembly not found with id: 1"));
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.update(agendaDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithInvalidDates() {
        agendaDTO.setStart(LocalDateTime.now().minusDays(2));
        agendaDTO.setEnd(LocalDateTime.now().minusDays(1));

        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> agendaService.update(agendaDTO));

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date or earlier than the current date."));
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithNullAssemblyId() {
        agendaDTO.setAssemblyId(null);

        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> agendaService.update(agendaDTO));

        assertEquals("Assembly ID must be provided", ex.getMessage());
        verifyNoInteractions(assemblyService, agendaRepository);
    }

    @Test
    void shouldDeleteAgendaById() {
        when(agendaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(agendaRepository).deleteById(1L);

        assertDoesNotThrow(() -> agendaService.deleteById(1L));

        verify(agendaRepository).existsById(1L);
        verify(agendaRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteAgendaNotFound() {
        when(agendaRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.deleteById(1L));

        assertEquals("Agenda not found with id: 1", ex.getMessage());
        verify(agendaRepository).existsById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }
}