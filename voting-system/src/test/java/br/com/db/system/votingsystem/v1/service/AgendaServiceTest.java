package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AssemblyRepository assemblyRepository;

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
        agenda.setStart(LocalDateTime.now().plusHours(1));
        agenda.setEnd(LocalDateTime.now().plusHours(2));

        agendaDTO = new AgendaDTO();
        agendaDTO.setId(1L);
        agendaDTO.setDescription("Test agenda");
        agendaDTO.setAssemblyId(1L);
        agendaDTO.setStart(agenda.getStart());
        agendaDTO.setEnd(agenda.getEnd());
    }

    @Test
    void shouldFindAllAgendas() {
        Page<Agenda> page = new PageImpl<>(List.of(agenda));
        when(agendaRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(agendaMapper.toDTO(any(Agenda.class))).thenReturn(agendaDTO);

        Page<AgendaDTO> result = agendaService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(agendaRepository).findAll(any(PageRequest.class));
    }

    @Test
    void shouldFindAgendaById() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaDTO);

        AgendaDTO result = agendaService.findById(1L);

        assertNotNull(result);
        assertEquals(agendaDTO.getId(), result.getId());
        verify(agendaRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> agendaService.findById(1L));

        assertEquals("Agenda not found with id: 1", ex.getMessage());
        verify(agendaRepository).findById(1L);
    }

    @Test
    void shouldCreateAgenda() {
        when(assemblyRepository.findById(1L)).thenReturn(Optional.of(assembly));
        when(agendaMapper.toEntity(agendaDTO)).thenReturn(agenda);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaDTO);

        AgendaDTO result = agendaService.create(agendaDTO);

        assertNotNull(result);
        assertEquals(agendaDTO.getDescription(), result.getDescription());
        verify(assemblyRepository).findById(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenCreateAgendaWithInvalidDescription() {
        agendaDTO.setDescription(" ");

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> agendaService.create(agendaDTO));

        assertEquals("Description must not be null or blank", ex.getMessage());
        verifyNoInteractions(assemblyRepository);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenCreateAgendaWithAssemblyNotFound() {
        when(assemblyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> agendaService.create(agendaDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(assemblyRepository).findById(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenCreateAgendaWithInvalidDates() {
        agendaDTO.setStart(LocalDateTime.now().minusDays(1));
        agendaDTO.setEnd(LocalDateTime.now().minusDays(2));

        when(assemblyRepository.findById(1L)).thenReturn(Optional.of(assembly));
        when(agendaMapper.toEntity(agendaDTO)).thenReturn(agenda);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> agendaService.create(agendaDTO));

        assertEquals("Start date cannot be later than the end date or earlier than the current date.", ex.getMessage());
        verify(assemblyRepository).findById(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldUpdateAgenda() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(assemblyRepository.findById(1L)).thenReturn(Optional.of(assembly));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaDTO);

        AgendaDTO result = agendaService.update(agendaDTO);

        assertNotNull(result);
        assertEquals(agendaDTO.getDescription(), result.getDescription());
        verify(agendaRepository).findById(1L);
        verify(assemblyRepository).findById(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithNullAssemblyId() {
        agendaDTO.setAssemblyId(null);

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> agendaService.update(agendaDTO));

        assertEquals("Assembly ID must be provided", ex.getMessage());
        verifyNoInteractions(agendaRepository);
        verifyNoInteractions(assemblyRepository);
    }

    @Test
    void shouldThrowWhenUpdateAgendaNotFound() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> agendaService.update(agendaDTO));

        assertEquals("Agenda not found with id: 1", ex.getMessage());
        verify(agendaRepository).findById(1L);
        verifyNoInteractions(assemblyRepository);
    }

    @Test
    void shouldThrowWhenUpdateAssemblyNotFound() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(assemblyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> agendaService.update(agendaDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(agendaRepository).findById(1L);
        verify(assemblyRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithInvalidDates() {
        agendaDTO.setStart(LocalDateTime.now().minusDays(2));
        agendaDTO.setEnd(LocalDateTime.now().minusDays(3));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(assemblyRepository.findById(1L)).thenReturn(Optional.of(assembly));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> agendaService.update(agendaDTO));

        assertEquals("Start date cannot be later than the end date or earlier than the current date.", ex.getMessage());
        verify(agendaRepository).findById(1L);
        verify(assemblyRepository).findById(1L);
    }

    @Test
    void shouldDeleteAgenda() {
        when(agendaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(agendaRepository).deleteById(1L);

        assertDoesNotThrow(() -> agendaService.deleteById(1L));

        verify(agendaRepository).existsById(1L);
        verify(agendaRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteAgendaNotFound() {
        when(agendaRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> agendaService.deleteById(1L));

        assertEquals("Agenda not found with id: 1", ex.getMessage());
        verify(agendaRepository).existsById(1L);
        verify(agendaRepository, never()).deleteById(1L);
    }
}