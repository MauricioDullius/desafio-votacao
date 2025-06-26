package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AgendaDTO {

    private Long id;
    private String description;

    private LocalDateTime start;
    private LocalDateTime end;

    private int state;
    List<Vote> votes;

    public AgendaDTO(Long id, String description, LocalDateTime start, LocalDateTime end, int state, List<Vote> votes) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.end = end;
        this.state = state;
        this.votes = votes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgendaDTO agenda = (AgendaDTO) o;
        return state == agenda.state && Objects.equals(id, agenda.id) && Objects.equals(description, agenda.description) && Objects.equals(start, agenda.start) && Objects.equals(end, agenda.end) && Objects.equals(votes, agenda.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, start, end, state, votes);
    }
}
