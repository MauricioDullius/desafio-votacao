package br.com.db.system.votingsystem.v1.dto;

import java.util.Objects;

public class VoteDTO {

    private Long id;
    private Long agendaId;
    private String memberCpf;
    private int vote;

    public VoteDTO() {}

    public VoteDTO(Long id, Long agendaId, String memberCpf, int vote) {
        this.id = id;
        this.agendaId = agendaId;
        this.memberCpf = memberCpf;
        this.vote = vote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public String getMemberCpf() {
        return memberCpf;
    }

    public void setMemberCpf(String memberCpf) {
        this.memberCpf = memberCpf;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteDTO voteDTO = (VoteDTO) o;
        return vote == voteDTO.vote &&
                Objects.equals(id, voteDTO.id) &&
                Objects.equals(agendaId, voteDTO.agendaId) &&
                Objects.equals(memberCpf, voteDTO.memberCpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agendaId, memberCpf, vote);
    }
}