package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.model.Member;

public interface VoteResolver {
    Agenda resolveAgenda(Long id);
    Member resolveMember(String cpf);
}