package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote/v1")
public class VoteController {

    @Autowired
    private VoteService service;

    @GetMapping
    public ResponseEntity<List<VoteDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<VoteDTO> create(@RequestBody VoteDTO voteDTO) throws Exception {
        VoteDTO created = service.create(voteDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping
    public ResponseEntity<VoteDTO> update(@RequestBody VoteDTO voteDTO) throws Exception {
        return ResponseEntity.ok(service.update(voteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}