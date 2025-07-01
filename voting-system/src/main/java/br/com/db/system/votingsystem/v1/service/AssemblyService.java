package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssemblyService {

    @Autowired
    private AssemblyRepository repository;

    @Autowired
    private AssemblyMapper mapper;

    public AssemblyDTO create(AssemblyDTO dto) {
        Assembly assembly = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(assembly));
    }

    public AssemblyDTO update(AssemblyDTO dto) {
        Assembly assembly = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assembly not found with id: " + dto.getId()));

        mapper.updateFromDTO(dto, assembly);

        return mapper.toDTO(repository.save(assembly));
    }

    public List<AssemblyDTO> findAll() {
        List<Assembly> assemblies = repository.findAll();
        return mapper.toDTOList(assemblies);
    }

    public AssemblyDTO findById(Long id) {
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assembly not found with id: " + id));
        return mapper.toDTO(assembly);
    }
}