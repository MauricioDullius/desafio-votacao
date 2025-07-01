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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendaMapper mapper;

    @Autowired
    private AssemblyRepository assemblyRepository;

    public List<AgendaDTO> findAll() {
        logger.info("Retrieving all agendas");
        List<AgendaDTO> agendas = repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
        logger.info("Found {} agendas", agendas.size());
        return agendas;
    }

    public AgendaDTO findById(Long id) {
        logger.info("Searching for agenda with id {}", id);
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", id);
                    return new ResourceNotFoundException("Agenda not found with id: " + id);
                });
        logger.info("Agenda found with id {}", id);
        return mapper.toDTO(agenda);
    }

    public AgendaDTO create(AgendaDTO dto) {
        logger.info("Creating agenda with description '{}'", dto.getDescription());
        validateAgendaDTO(dto);

        Assembly assembly = assemblyRepository.findById(dto.getAssemblyId())
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", dto.getAssemblyId());
                    return new ResourceNotFoundException("Assembly not found with id: " + dto.getAssemblyId());
                });

        Agenda agenda = mapper.toEntity(dto);
        agenda.setAssembly(assembly);
        agenda.setStart(dto.getStart() == null ? LocalDateTime.now() : dto.getStart());
        agenda.setEnd(dto.getEnd() == null ? agenda.getStart().plusMinutes(1) : dto.getEnd());

        validateData(agenda.getStart(), agenda.getEnd());

        agenda = repository.save(agenda);
        logger.info("Agenda created successfully with id {}", agenda.getId());

        return mapper.toDTO(agenda);
    }

    public AgendaDTO update(AgendaDTO dto) {
        logger.info("Updating agenda with id {}", dto.getId());
        if (dto.getAssemblyId() == null) {
            logger.warn("Invalid request: Assembly ID must be provided");
            throw new InvalidRequestException("Assembly ID must be provided");
        }

        Agenda agenda = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", dto.getId());
                    return new ResourceNotFoundException("Agenda not found with id: " + dto.getId());
                });

        Assembly assembly = assemblyRepository.findById(dto.getAssemblyId())
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", dto.getAssemblyId());
                    return new ResourceNotFoundException("Assembly not found with id: " + dto.getAssemblyId());
                });

        agenda.setDescription(dto.getDescription());
        agenda.setStart(dto.getStart());
        agenda.setEnd(dto.getEnd());
        agenda.setAssembly(assembly);

        validateData(agenda.getStart(), agenda.getEnd());

        agenda = repository.save(agenda);
        logger.info("Agenda updated successfully with id {}", agenda.getId());

        return mapper.toDTO(agenda);
    }

    public void deleteById(Long id) {
        logger.info("Deleting agenda with id {}", id);
        if (!repository.existsById(id)) {
            logger.error("Agenda not found with id {}", id);
            throw new ResourceNotFoundException("Agenda not found with id: " + id);
        }
        repository.deleteById(id);
        logger.info("Agenda deleted with id {}", id);
    }

    private void validateData(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())) {
            logger.warn("Invalid dates: start={} end={}", start, end);
            throw new BusinessRuleException("Start date cannot be later than the end date or earlier than the current date.");
        }
    }

    private void validateAgendaDTO(AgendaDTO dto) {
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            logger.warn("Invalid request: description is null or blank");
            throw new InvalidRequestException("Description must not be null or blank");
        }
        if (dto.getAssemblyId() == null) {
            logger.warn("Invalid request: AssemblyId is null or blank");
            throw new InvalidRequestException("AssemblyId must not be null or blank");
        }
    }
}