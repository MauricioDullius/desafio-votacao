package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssemblyService {

    @Autowired
    private AssemblyRepository repository;

    @Autowired
    private AssemblyMapper mapper;

    public AssemblyDTO create(AssemblyDTO dto) {
        validateAssemblyDTO(dto);

        Assembly assembly = mapper.toEntity(dto);
        validateDates(assembly.getStart(), assembly.getEnd());

        return mapper.toDTO(repository.save(assembly));
    }

    public AssemblyDTO update(AssemblyDTO dto) {
        validateAssemblyDTO(dto);

        Assembly assembly = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assembly not found with id: " + dto.getId()));

        mapper.updateFromDTO(dto, assembly);
        validateDates(assembly.getStart(), assembly.getEnd());

        return mapper.toDTO(repository.save(assembly));
    }

    public List<AssemblyDTO> findAll() {
        List<Assembly> assemblies = repository.findAll();
        return mapper.toDTOList(assemblies);
    }

    public AssemblyDTO findById(Long id) {
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assembly not found with id: " + id));
        return mapper.toDTO(assembly);
    }

    private void validateAssemblyDTO(AssemblyDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidRequestException("Name must not be null or blank");
        }
        if (dto.getStart() == null) {
            throw new InvalidRequestException("Start date must be provided");
        }
        if (dto.getEnd() == null) {
            throw new InvalidRequestException("End date must be provided");
        }
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Start date cannot be later than the end date or earlier than the current date.");
        }
    }
}