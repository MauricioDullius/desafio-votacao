package br.com.db.system.votingsystem.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AssemblyDTO {

    private Long id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<AgendaDTO> agendas;
}