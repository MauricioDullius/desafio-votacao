package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.util.DateUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendaMapper mapper;

    @Autowired
    private AssemblyService assemblyService;

    public Page<AgendaDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all agendas");

        Page<Agenda> agendaPage = repository.findAll(pageable);

        Page<AgendaDTO> agendaDTOPage = agendaPage.map(mapper::toDTO);

        logger.info("Found {} agendas", agendaDTOPage.getNumberOfElements());

        return agendaDTOPage;
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
    public Agenda findByIdEntity(Long id) {
        logger.info("Searching for agenda with id {}", id);
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", id);
                    return new ResourceNotFoundException("Agenda not found with id: " + id);
                });
        logger.info("Agenda found with id {}", id);
        return agenda;
    }

    public AgendaDTO create(AgendaDTO dto) {
        logger.info("Creating agenda with description '{}'", dto.getDescription());

        if( dto.getId() != null ) dto.setId(null);

        Assembly assembly = assemblyService.findByIdEntity(dto.getAssemblyId());

        Agenda agenda = mapper.toEntity(dto);
        agenda.setAssembly(assembly);
        agenda.setStart(dto.getStart() == null ? LocalDateTime.now() : dto.getStart());
        agenda.setEnd(dto.getEnd() == null ? agenda.getStart().plusMinutes(1) : dto.getEnd());

        DateUtils.validateDates(agenda.getStart(), agenda.getEnd());

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

        Assembly assembly = assemblyService.findByIdEntity(dto.getAssemblyId());

        agenda.setDescription(dto.getDescription());
        agenda.setStart(dto.getStart());
        agenda.setEnd(dto.getEnd());
        agenda.setAssembly(assembly);

        DateUtils.validateDates(agenda.getStart(), agenda.getEnd());

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
}