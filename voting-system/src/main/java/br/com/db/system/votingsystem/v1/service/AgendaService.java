package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendaMapper mapper;

    @Autowired
    private AssemblyRepository assemblyRepository;

    public List<AgendaDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public AgendaDTO findById(Long id) {
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agenda not found with id: " + id));
        return mapper.toDTO(agenda);
    }

    public AgendaDTO create(AgendaDTO dto) throws Exception {
        Assembly assembly = assemblyRepository.findById(dto.getAssemblyId())
                .orElseThrow(() -> new EntityNotFoundException("Assembly not found with id: " + dto.getAssemblyId()));

        Agenda agenda = mapper.toEntity(dto);
        agenda.setAssembly(assembly);
        agenda.setStart( dto.getStart() == null ? LocalDateTime.now() : dto.getStart() );
        agenda.setEnd( dto.getEnd() == null ? agenda.getStart().plusMinutes(1) : dto.getEnd() );

        validateData(agenda.getStart(), agenda.getEnd());

        agenda = repository.save(agenda);

        return mapper.toDTO(agenda);
    }

    public AgendaDTO update(AgendaDTO dto) throws Exception {
        if (dto.getAssemblyId() == null) {
            throw new IllegalArgumentException("Assembly ID must be provided");
        }

        Agenda agenda = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Agenda not found with id: " + dto.getId()));

        Assembly assembly = assemblyRepository.findById(dto.getAssemblyId())
                .orElseThrow(() -> new EntityNotFoundException("Assembly not found with id: " + dto.getAssemblyId()));

        agenda.setDescription(dto.getDescription());
        agenda.setStart(dto.getStart());
        agenda.setEnd(dto.getEnd());
        agenda.setAssembly(assembly);

        validateData(agenda.getStart(), agenda.getEnd());
        agenda = repository.save(agenda);

        return mapper.toDTO(agenda);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateData(LocalDateTime start, LocalDateTime end) throws Exception {
        if( end.isBefore(start) && start.isBefore(LocalDateTime.now()) ) {
            throw new Exception("Start date cannot be later than the end date or earlier than the current date.");
        }
    }
}