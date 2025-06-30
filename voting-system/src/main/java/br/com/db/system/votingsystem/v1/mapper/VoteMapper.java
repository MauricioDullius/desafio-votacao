package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.model.Vote;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "agendaId", source = "agenda.id")
    @Mapping(target = "memberCpf", source = "member.cpf")
    VoteDTO toDTO(Vote vote);

    @Mapping(target = "agenda", expression = "java(resolver.resolveAgenda(dto.getAgendaId()))")
    @Mapping(target = "member", expression = "java(resolver.resolveMember(dto.getMemberCpf()))")
    Vote toEntity(VoteDTO dto, @Context VoteResolver resolver);

    List<VoteDTO> toDTOList(List<Vote> votes);
}
