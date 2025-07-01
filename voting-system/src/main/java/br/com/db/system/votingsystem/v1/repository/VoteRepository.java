package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByAgendaId(Long agendaId);

    boolean existsByMemberIdAndAgendaId(Long memberId, Long agendaId);
}

