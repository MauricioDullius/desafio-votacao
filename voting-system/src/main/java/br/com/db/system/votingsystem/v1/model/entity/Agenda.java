package br.com.db.system.votingsystem.v1.model.entity;

import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String description;

    @Column
    private LocalDateTime start;

    @Column
    private LocalDateTime end;

    @Transient
    private AgendaState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assembly_id", nullable = false)
    private Assembly assembly;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    public AgendaState getState() {
        if (end != null && LocalDateTime.now().isBefore(end)) {
            return AgendaState.IN_VOTING;
        }

        long yesVotes = votes.stream()
                .filter(v -> v.getVote() == VoteState.YES)
                .count();

        long noVotes = votes.stream()
                .filter(v -> v.getVote() == VoteState.NO)
                .count();

        return yesVotes > noVotes ? AgendaState.APPROVED : AgendaState.REJECTED;
    }
}