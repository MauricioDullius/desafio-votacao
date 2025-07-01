package br.com.db.system.votingsystem.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String name;
    private String cpf;
    private boolean active;
}
