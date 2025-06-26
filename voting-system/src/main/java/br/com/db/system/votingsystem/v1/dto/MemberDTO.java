package br.com.db.system.votingsystem.v1.dto;

import java.util.Objects;

public class MemberDTO {

    private Long id;
    private String name;
    private String cpf;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public MemberDTO(Long id, String name, String cpf, boolean active) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO member = (MemberDTO) o;
        return active == member.active && Objects.equals(id, member.id) && Objects.equals(name, member.name) && Objects.equals(cpf, member.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cpf, active);
    }
}
