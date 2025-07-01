package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.VoteMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.model.entity.Vote;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import br.com.db.system.votingsystem.v1.repository.VoteRepository;

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
    private AgendaRepository agendaRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<VoteDTO> findAll() {
        return voteMapper.toDTOList(repository.findAll());
    }

    public List<VoteDTO> findByAgendaId(Long agendaId) {
        return voteMapper.toDTOList(repository.findByAgendaId(agendaId));
    }

    public VoteDTO findById(Long id) {
        Vote vote = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found with id: " + id));
        return voteMapper.toDTO(vote);
    }

    public VoteDTO update(VoteDTO dto) {
        validateVoteDTO(dto);

        Vote vote = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found with id: " + dto.getId()));

        Member member = memberRepository.findMemberByCpf(dto.getMemberCpf());
        if (member == null) {
            throw new ResourceNotFoundException("Member not found with CPF: " + dto.getMemberCpf());
        }

        vote.setVote(dto.getVote());
        vote.setMember(member);

        return voteMapper.toDTO(repository.save(vote));
    }

    public VoteDTO create(VoteDTO dto) {
        validateVoteDTO(dto);

        Member member = memberRepository.findMemberByCpf(dto.getMemberCpf());
        if (member == null) {
            throw new ResourceNotFoundException("Member not found with CPF: " + dto.getMemberCpf());
        }

        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> new ResourceNotFoundException("Agenda not found with id: " + dto.getAgendaId()));

        boolean hasVoted = repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId());
        if (hasVoted) {
            throw new BusinessRuleException("This member has already voted on this agenda");
        }

        Vote vote = voteMapper.toEntity(dto);
        vote.setAgenda(agenda);
        vote.setMember(member);

        return voteMapper.toDTO(repository.save(vote));
    }

    public void deleteById(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Vote not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private void validateVoteDTO(VoteDTO dto) {
        if (dto.getAgendaId() == null) {
            throw new InvalidRequestException("AgendaId must be provided");
        }
        if (dto.getMemberCpf() == null || dto.getMemberCpf().isBlank()) {
            throw new InvalidRequestException("Member CPF must not be null or blank");
        }
        if (dto.getVote() == null) {
            throw new InvalidRequestException("Vote must be provided");
        }
        boolean validVote = false;
        for (VoteState state : VoteState.values()) {
            if (state == dto.getVote()) {
                validVote = true;
                break;
            }
        }
        if (!validVote) {
            throw new InvalidRequestException("Vote must be a valid VoteState");
        }
    }
}