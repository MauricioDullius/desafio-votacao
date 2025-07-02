package br.com.db.system.votingsystem.v1.model.entity;

import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AgendaTest {

    @Test
    public void testGetStateInVotingWhenCurrentBeforeEnd() {
        Agenda agenda = new Agenda();
        agenda.setEnd(LocalDateTime.now().plusMinutes(10));

        AgendaState state = agenda.getState();

        assertThat(state).isEqualTo(AgendaState.IN_VOTING);
    }

    @Test
    public void testGetStateApprovedWhenYesVotesGreater() {
        Agenda agenda = new Agenda();
        agenda.setEnd(LocalDateTime.now().minusMinutes(10));

        Vote voteYes1 = new Vote();
        voteYes1.setVote(VoteState.YES);

        Vote voteYes2 = new Vote();
        voteYes2.setVote(VoteState.YES);

        Vote voteNo = new Vote();
        voteNo.setVote(VoteState.NO);

        agenda.setVotes(List.of(voteYes1, voteYes2, voteNo));

        AgendaState state = agenda.getState();

        assertThat(state).isEqualTo(AgendaState.APPROVED);
    }

    @Test
    public void testGetStateRejectedWhenNoVotesGreaterOrEqual() {
        Agenda agenda = new Agenda();
        agenda.setEnd(LocalDateTime.now().minusMinutes(10));

        Vote voteYes = new Vote();
        voteYes.setVote(VoteState.YES);

        Vote voteNo1 = new Vote();
        voteNo1.setVote(VoteState.NO);

        Vote voteNo2 = new Vote();
        voteNo2.setVote(VoteState.NO);

        agenda.setVotes(List.of(voteYes, voteNo1, voteNo2));

        AgendaState state = agenda.getState();

        assertThat(state).isEqualTo(AgendaState.REJECTED);
    }

    @Test
    public void testGettersAndSetters() {
        Agenda agenda = new Agenda();
        agenda.setId(123L);
        agenda.setDescription("description");
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        agenda.setStart(start);
        agenda.setEnd(end);

        Assembly assembly = new Assembly();
        agenda.setAssembly(assembly);

        assertThat(agenda.getId()).isEqualTo(123L);
        assertThat(agenda.getDescription()).isEqualTo("description");
        assertThat(agenda.getStart()).isEqualTo(start);
        assertThat(agenda.getEnd()).isEqualTo(end);
        assertThat(agenda.getAssembly()).isEqualTo(assembly);
    }
}