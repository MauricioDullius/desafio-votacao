package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteDTO {

    private Long id;
    private Long agendaId;
    private String memberCpf;
    private VoteState vote;
}