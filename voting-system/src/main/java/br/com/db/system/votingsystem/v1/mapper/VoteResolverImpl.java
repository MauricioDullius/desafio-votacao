package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.model.Member;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteResolverImpl implements VoteResolver {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Agenda resolveAgenda(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agenda not found with id: " + id));
    }

    @Override
    public Member resolveMember(String cpf) {
        Member member = memberRepository.findMemberByCpf(cpf);
        if (member == null) {
            throw new EntityNotFoundException("Member not found with CPF: " + cpf);
        }
        return member;
    }
}