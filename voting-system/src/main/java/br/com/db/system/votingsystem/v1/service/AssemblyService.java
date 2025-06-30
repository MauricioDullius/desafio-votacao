package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.Assembly;
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

        assembly.setName(dto.getName());
        assembly.setStart(dto.getStart());
        assembly.setEnd(dto.getEnd());

        return mapper.toDTO(repository.save(assembly));
    }

    public List<AssemblyDTO> findAll() {
        return mapper.toDTOList(repository.findAll());
    }

    public AssemblyDTO findById(Long id) {
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assembly not found with id: " + id));
        return mapper.toDTO(assembly);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}