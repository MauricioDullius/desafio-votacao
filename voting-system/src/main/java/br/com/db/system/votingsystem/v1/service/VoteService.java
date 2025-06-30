package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.mapper.VoteMapper;
import br.com.db.system.votingsystem.v1.mapper.VoteResolver;
import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.model.Member;
import br.com.db.system.votingsystem.v1.model.Vote;
import br.com.db.system.votingsystem.v1.repository.VoteRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteResolver voteResolver;

    public List<VoteDTO> findAll() {
        return voteMapper.toDTOList(repository.findAll());
    }

    public List<VoteDTO> findByAgendaId(Long agendaId) {
        return voteMapper.toDTOList(repository.findByAgendaId(agendaId));
    }

    public VoteDTO findById(Long id) {
        Vote vote = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found with id: " + id));
        return voteMapper.toDTO(vote);
    }

    public VoteDTO update(VoteDTO dto) {
        Vote vote = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vote not found with id: " + dto.getId()));

        vote.setVote(dto.getVote());
        vote.setMember(voteResolver.resolveMember(dto.getMemberCpf()));

        return voteMapper.toDTO(repository.save(vote));
    }

    public VoteDTO create(VoteDTO dto) {
        Member member = voteResolver.resolveMember(dto.getMemberCpf());
        Agenda agenda = voteResolver.resolveAgenda(dto.getAgendaId());

        boolean hasVoted = repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId());
        if (hasVoted) {
            throw new RuntimeException("This member has already voted on this agenda");
        }

        Vote vote = voteMapper.toEntity(dto, voteResolver);
        return voteMapper.toDTO(repository.save(vote));
    }

    public List<VoteDTO> findVotesByAgendaId(Long agendaId) {
        return voteMapper.toDTOList(repository.findByAgendaId(agendaId));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}