package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda/v1")
public class AgendaController {

    @Autowired
    private AgendaService service;

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgendaDTO> create(@RequestBody AgendaDTO agendaDTO) throws Exception {
        return ResponseEntity.status(201).body(service.create(agendaDTO));
    }

    @PutMapping
    public ResponseEntity<AgendaDTO> update(@RequestBody AgendaDTO agendaDTO) throws Exception {
        return ResponseEntity.ok(service.update(agendaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
