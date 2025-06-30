package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.Agenda;

import java.time.LocalDateTime;
import java.util.List;

public class AssemblyDTO {

    private Long id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Agenda> agendas;

    public AssemblyDTO() {}

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}