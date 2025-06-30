package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.model.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import org.springframework.stereotype.Component;

@Component
public class AssemblyResolverImpl implements AssemblyResolver {

    private final AssemblyRepository assemblyRepository;

    public AssemblyResolverImpl(AssemblyRepository assemblyRepository) {
        this.assemblyRepository = assemblyRepository;
    }

    @Override
    public Assembly resolve(Long id) {
        return assemblyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assembly not found with id: " + id));
    }
}