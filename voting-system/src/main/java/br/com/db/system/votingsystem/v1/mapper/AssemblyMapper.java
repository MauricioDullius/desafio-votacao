package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.model.Assembly;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssemblyMapper {

    AssemblyDTO toDTO(Assembly assembly);

    Assembly toEntity(AssemblyDTO dto);

    List<AssemblyDTO> toDTOList(List<Assembly> assemblies);
}