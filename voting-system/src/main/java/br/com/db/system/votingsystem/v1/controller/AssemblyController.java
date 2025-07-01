package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.service.AssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assembly/v1")
public class AssemblyController {

    @Autowired
    private AssemblyService service;

    @GetMapping
    public ResponseEntity<List<AssemblyDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssemblyDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AssemblyDTO> create(@RequestBody AssemblyDTO assemblyDTO) throws Exception {
        AssemblyDTO created = service.create(assemblyDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping
    public ResponseEntity<AssemblyDTO> update(@RequestBody AssemblyDTO assemblyDTO) {
        return ResponseEntity.ok(service.update(assemblyDTO));
    }
}