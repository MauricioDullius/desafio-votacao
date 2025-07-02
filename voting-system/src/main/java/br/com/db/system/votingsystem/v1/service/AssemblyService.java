package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssemblyService {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyService.class);

    @Autowired
    private AssemblyRepository repository;

    @Autowired
    private AssemblyMapper mapper;

    public AssemblyDTO create(AssemblyDTO dto) {
        logger.info("Creating assembly with name '{}'", dto.getName());
        validateAssemblyDTO(dto);

        Assembly assembly = mapper.toEntity(dto);
        validateDates(assembly.getStart(), assembly.getEnd());

        Assembly saved = repository.save(assembly);
        logger.info("Assembly created successfully with id {}", saved.getId());

        return mapper.toDTO(saved);
    }

    public AssemblyDTO update(AssemblyDTO dto) {
        logger.info("Updating assembly with id {}", dto.getId());
        validateAssemblyDTO(dto);

        Assembly assembly = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", dto.getId());
                    return new ResourceNotFoundException("Assembly not found with id: " + dto.getId());
                });

        mapper.updateFromDTO(dto, assembly);
        validateDates(assembly.getStart(), assembly.getEnd());

        Assembly updated = repository.save(assembly);
        logger.info("Assembly updated successfully with id {}", updated.getId());

        return mapper.toDTO(updated);
    }

    public Page<AssemblyDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all assemblies");
        Page<Assembly> assembliesPage = repository.findAll(pageable);
        logger.info("Found {} assemblies", assembliesPage.getNumberOfElements());
        return assembliesPage.map(mapper::toDTO);
    }

    public AssemblyDTO findById(Long id) {
        logger.info("Searching for assembly with id {}", id);
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", id);
                    return new ResourceNotFoundException("Assembly not found with id: " + id);
                });
        logger.info("Assembly found with id {}", id);
        return mapper.toDTO(assembly);
    }

    private void validateAssemblyDTO(AssemblyDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            logger.warn("Invalid request: Name is null or blank");
            throw new InvalidRequestException("Name must not be null or blank");
        }
        if (dto.getStart() == null) {
            logger.warn("Invalid request: Start date is null");
            throw new InvalidRequestException("Start date must be provided");
        }
        if (dto.getEnd() == null) {
            logger.warn("Invalid request: End date is null");
            throw new InvalidRequestException("End date must be provided");
        }
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())) {
            logger.warn("Invalid dates: start={} end={}", start, end);
            throw new InvalidRequestException("Start date cannot be later than the end date or earlier than the current date.");
        }
    }
}