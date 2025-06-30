package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.model.Agenda;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(target = "assembly", expression = "java(agenda.getAssembly().getId())")
    AgendaDTO toDTO(Agenda agenda);

    @Mapping(target = "assembly", expression = "java(resolver.resolve(dto.getAssembly()))")
    Agenda toEntity(AgendaDTO dto, @Context AssemblyResolver resolver);

    List<AgendaDTO> toDTOList(List<Agenda> agendas);
}