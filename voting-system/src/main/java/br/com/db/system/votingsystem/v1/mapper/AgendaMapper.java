package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(target = "assemblyId", source = "assembly.id")
    AgendaDTO toDTO(Agenda agenda);

    @Mapping(target = "assembly", ignore = true)
    Agenda toEntity(AgendaDTO dto);

    List<AgendaDTO> toDTOList(List<Agenda> agendas);
}
