package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.Agenda;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AssemblyDTO {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    private List<Agenda> agendas;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssemblyDTO assembly = (AssemblyDTO) o;
        return Objects.equals(id, assembly.id) && Objects.equals(start, assembly.start) && Objects.equals(end, assembly.end) && Objects.equals(agendas, assembly.agendas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, agendas);
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

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

    public AssemblyDTO(Long id, LocalDateTime start, LocalDateTime end, List<Agenda> agendas) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.agendas = agendas;
    }
}
