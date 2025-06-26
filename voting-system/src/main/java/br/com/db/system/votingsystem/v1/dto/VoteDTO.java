package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.model.Member;

import java.util.Objects;

public class VoteDTO {

    private Long id;
    private Agenda agenda;
    private Member member;
    private int  vote;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VoteDTO vote1 = (VoteDTO) o;
        return vote == vote1.vote && Objects.equals(id, vote1.id) && Objects.equals(agenda, vote1.agenda) && Objects.equals(member, vote1.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agenda, member, vote);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
