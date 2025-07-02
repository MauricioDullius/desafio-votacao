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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private VoteMapper mapper;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAllVotesSuccessfully() {
        Vote vote = new Vote();
        vote.setId(1L);

        VoteDTO dto = new VoteDTO();
        dto.setId(1L);

        Page<Vote> page = new PageImpl<>(List.of(vote));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDTO(vote)).thenReturn(dto);

        Page<VoteDTO> result = service.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(any(Pageable.class));
        verify(mapper).toDTO(vote);
    }

    @Test
    void shouldFindVotesByAgendaIdSuccessfully() {
        Long agendaId = 10L;
        Vote vote = new Vote();
        vote.setId(2L);

        VoteDTO dto = new VoteDTO();
        dto.setId(2L);

        when(repository.findByAgendaId(agendaId)).thenReturn(List.of(vote));
        when(mapper.toDTOList(List.of(vote))).thenReturn(List.of(dto));

        List<VoteDTO> result = service.findByAgendaId(agendaId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByAgendaId(agendaId);
        verify(mapper).toDTOList(anyList());
    }

    @Test
    void shouldFindVoteByIdSuccessfully() {
        Long id = 5L;

        Vote vote = new Vote();
        vote.setId(id);

        VoteDTO dto = new VoteDTO();
        dto.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(vote));
        when(mapper.toDTO(vote)).thenReturn(dto);

        VoteDTO result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findById(id);
        verify(mapper).toDTO(vote);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(id);
        });

        assertEquals("Vote not found with id: " + id, ex.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void shouldCreateVoteSuccessfully() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(100L);

        Agenda agenda = new Agenda();
        agenda.setId(1L);

        Vote voteEntity = new Vote();

        Vote savedVote = new Vote();
        savedVote.setId(10L);

        VoteDTO savedDto = new VoteDTO();
        savedDto.setId(10L);

        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(member);
        when(agendaRepository.findById(dto.getAgendaId())).thenReturn(Optional.of(agenda));
        when(repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(voteEntity);
        when(repository.save(voteEntity)).thenReturn(savedVote);
        when(mapper.toDTO(savedVote)).thenReturn(savedDto);

        VoteDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(savedDto.getId(), result.getId());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
        verify(agendaRepository).findById(dto.getAgendaId());
        verify(repository).existsByMemberIdAndAgendaId(member.getId(), agenda.getId());
        verify(repository).save(voteEntity);
        verify(mapper).toDTO(savedVote);
    }

    @Test
    void shouldThrowWhenCreateVoteMemberNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("00000000000");
        dto.setVote(VoteState.YES);

        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });

        assertEquals("Member not found with CPF: " + dto.getMemberCpf(), ex.getMessage());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
    }

    @Test
    void shouldThrowWhenCreateVoteAgendaNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(100L);

        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(member);
        when(agendaRepository.findById(dto.getAgendaId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });

        assertEquals("Agenda not found with id: " + dto.getAgendaId(), ex.getMessage());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
        verify(agendaRepository).findById(dto.getAgendaId());
    }

    @Test
    void shouldThrowWhenCreateVoteMemberAlreadyVoted() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(100L);

        Agenda agenda = new Agenda();
        agenda.setId(1L);

        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(member);
        when(agendaRepository.findById(dto.getAgendaId())).thenReturn(Optional.of(agenda));
        when(repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId())).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            service.create(dto);
        });

        assertEquals("This member has already voted on this agenda", ex.getMessage());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
        verify(agendaRepository).findById(dto.getAgendaId());
        verify(repository).existsByMemberIdAndAgendaId(member.getId(), agenda.getId());
    }

    @Test
    void shouldUpdateVoteSuccessfully() {
        VoteDTO dto = new VoteDTO();
        dto.setId(5L);
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.NO);

        Member member = new Member();
        member.setId(100L);

        Vote voteEntity = new Vote();
        voteEntity.setId(dto.getId());

        Vote updatedVote = new Vote();
        updatedVote.setId(dto.getId());

        VoteDTO updatedDto = new VoteDTO();
        updatedDto.setId(dto.getId());

        when(repository.findById(dto.getId())).thenReturn(Optional.of(voteEntity));
        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(member);
        when(repository.save(voteEntity)).thenReturn(updatedVote);
        when(mapper.toDTO(updatedVote)).thenReturn(updatedDto);

        VoteDTO result = service.update(dto);

        assertNotNull(result);
        assertEquals(updatedDto.getId(), result.getId());
        verify(repository).findById(dto.getId());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
        verify(repository).save(voteEntity);
        verify(mapper).toDTO(updatedVote);
    }

    @Test
    void shouldThrowWhenUpdateVoteNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setId(99L);
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        assertEquals("Vote not found with id: " + dto.getId(), ex.getMessage());
        verify(repository).findById(dto.getId());
    }

    @Test
    void shouldThrowWhenUpdateVoteMemberNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setId(5L);
        dto.setAgendaId(1L);
        dto.setMemberCpf("00000000000");
        dto.setVote(VoteState.YES);

        Vote voteEntity = new Vote();
        voteEntity.setId(dto.getId());

        when(repository.findById(dto.getId())).thenReturn(Optional.of(voteEntity));
        when(memberRepository.findMemberByCpf(dto.getMemberCpf())).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        assertEquals("Member not found with CPF: " + dto.getMemberCpf(), ex.getMessage());
        verify(repository).findById(dto.getId());
        verify(memberRepository).findMemberByCpf(dto.getMemberCpf());
    }

    @Test
    void shouldDeleteVoteSuccessfully() {
        Long id = 7L;

        when(repository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> {
            service.deleteById(id);
        });

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteVoteNotFound() {
        Long id = 77L;

        when(repository.existsById(id)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteById(id);
        });

        assertEquals("Vote not found with id: " + id, ex.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(id);
    }

    @Test
    void shouldThrowInvalidRequestExceptionForInvalidDTO() {
        VoteDTO dto = new VoteDTO();

        dto.setAgendaId(null);
        dto.setMemberCpf("123");
        dto.setVote(VoteState.YES);

        InvalidRequestException ex1 = assertThrows(InvalidRequestException.class, () -> {
            service.create(dto);
        });
        assertEquals("AgendaId must be provided", ex1.getMessage());

        dto.setAgendaId(1L);
        dto.setMemberCpf("");
        dto.setVote(VoteState.YES);

        InvalidRequestException ex2 = assertThrows(InvalidRequestException.class, () -> {
            service.create(dto);
        });
        assertEquals("Member CPF must not be null or blank", ex2.getMessage());

        dto.setMemberCpf("123");
        dto.setVote(null);

        InvalidRequestException ex3 = assertThrows(InvalidRequestException.class, () -> {
            service.create(dto);
        });
        assertEquals("Vote must be provided", ex3.getMessage());
    }
}