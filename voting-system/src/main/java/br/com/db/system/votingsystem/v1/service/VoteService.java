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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteMapper mapper;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Page<VoteDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all votes");
        Page<Vote> votesPage = repository.findAll(pageable);
        logger.info("Found {} votes", votesPage.getNumberOfElements());
        return votesPage.map(mapper::toDTO);
    }

    public List<VoteDTO> findByAgendaId(Long agendaId) {
        logger.info("Retrieving votes for agendaId {}", agendaId);
        List<Vote> votes = repository.findByAgendaId(agendaId);
        logger.info("Found {} votes for agendaId {}", votes.size(), agendaId);
        return mapper.toDTOList(votes);
    }

    public VoteDTO findById(Long id) {
        logger.info("Searching vote by id {}", id);
        Vote vote = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Vote not found with id {}", id);
                    return new ResourceNotFoundException("Vote not found with id: " + id);
                });
        logger.info("Vote found with id {}", id);
        return mapper.toDTO(vote);
    }

    public VoteDTO update(VoteDTO dto) {
        logger.info("Updating vote with id {}", dto.getId());
        validateVoteDTO(dto);

        Vote vote = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Vote not found with id {}", dto.getId());
                    return new ResourceNotFoundException("Vote not found with id: " + dto.getId());
                });

        Member member = memberRepository.findMemberByCpf(dto.getMemberCpf());
        if (member == null) {
            logger.error("Member not found with CPF {}", dto.getMemberCpf());
            throw new ResourceNotFoundException("Member not found with CPF: " + dto.getMemberCpf());
        }

        vote.setVote(dto.getVote());
        vote.setMember(member);

        Vote updated = repository.save(vote);
        logger.info("Vote updated successfully with id {}", updated.getId());
        return mapper.toDTO(updated);
    }

    public VoteDTO create(VoteDTO dto) {
        logger.info("Creating vote for member CPF {} on agendaId {}", dto.getMemberCpf(), dto.getAgendaId());
        validateVoteDTO(dto);

        Member member = memberRepository.findMemberByCpf(dto.getMemberCpf());
        if (member == null) {
            logger.error("Member not found with CPF {}", dto.getMemberCpf());
            throw new ResourceNotFoundException("Member not found with CPF: " + dto.getMemberCpf());
        }

        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", dto.getAgendaId());
                    return new ResourceNotFoundException("Agenda not found with id: " + dto.getAgendaId());
                });

        boolean hasVoted = repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId());
        if (hasVoted) {
            logger.error("Member CPF {} has already voted on agendaId {}", dto.getMemberCpf(), dto.getAgendaId());
            throw new BusinessRuleException("This member has already voted on this agenda");
        }

        Vote vote = mapper.toEntity(dto);
        vote.setAgenda(agenda);
        vote.setMember(member);

        Vote saved = repository.save(vote);
        logger.info("Vote created successfully with id {}", saved.getId());
        return mapper.toDTO(saved);
    }

    public void deleteById(Long id) {
        logger.info("Deleting vote with id {}", id);
        if (!repository.existsById(id)) {
            logger.error("Vote not found with id {}", id);
            throw new ResourceNotFoundException("Vote not found with id: " + id);
        }
        repository.deleteById(id);
        logger.info("Vote deleted with id {}", id);
    }

    private void validateVoteDTO(VoteDTO dto) {
        if (dto.getAgendaId() == null) {
            logger.warn("Invalid request: AgendaId not provided");
            throw new InvalidRequestException("AgendaId must be provided");
        }
        if (dto.getMemberCpf() == null || dto.getMemberCpf().isBlank()) {
            logger.warn("Invalid request: Member CPF is null or blank");
            throw new InvalidRequestException("Member CPF must not be null or blank");
        }
        if (dto.getVote() == null) {
            logger.warn("Invalid request: Vote not provided");
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
            logger.warn("Invalid request: Vote is not a valid VoteState");
            throw new InvalidRequestException("Vote must be a valid VoteState");
        }
    }
}