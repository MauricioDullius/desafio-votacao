package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.mapper.AssemblyResolver;
import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendaMapper agendaMapper;

    @Autowired
    private AssemblyResolver assemblyResolver;

    public List<AgendaDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(agenda -> agendaMapper.toDTO(agenda))
                .toList();
    }

    public AgendaDTO findById(Long id) {
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agenda not found with id: " + id));

        return agendaMapper.toDTO(agenda);
    }

    public AgendaDTO create(AgendaDTO dto) {
        Agenda agenda = agendaMapper.toEntity(dto, assemblyResolver);
        agenda = repository.save(agenda);
        return agendaMapper.toDTO(agenda);
    }

    public AgendaDTO update(AgendaDTO dto) throws Exception {
        Long agendaId = dto.getId();

        Agenda agenda = repository.findById(agendaId)
                .orElseThrow(() -> new EntityNotFoundException("Agenda not found with id: " + agendaId));

        if (dto.getAssembly() == null) {
            throw new Exception("Assembly ID must be provided");
        }

        agenda.setDescription(dto.getDescription());
        agenda.setStart(dto.getStart());
        agenda.setEnd(dto.getEnd());
        agenda.setState(dto.getState());

        agenda.setAssembly(assemblyResolver.resolve(dto.getAssembly()));

        agenda = repository.save(agenda);

        return agendaMapper.toDTO(agenda);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}