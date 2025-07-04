package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AgendaDTO {

    private Long id;

    @NotBlank(message = "Description must not be null or blank")
    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull(message = "State must not be null")
    private AgendaState state;

    private Long assemblyId;
}