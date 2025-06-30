package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.Assembly;
import br.com.db.system.votingsystem.v1.model.Vote;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AgendaDTO {

    private Long id;
    private String description;

    //aplicar data e hora formatada (mudar para string o dto) não setar a hora de inicio, apenas o tempo de duração da pauta
    private LocalDateTime start;
    private LocalDateTime end;
    private int state; //converter em enum
    private Long assembly;

    public AgendaDTO() {}

    public Long getAssembly() {
        return assembly;
    }

    public void setAssembly(Long assembly) {
        this.assembly = assembly;
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
}