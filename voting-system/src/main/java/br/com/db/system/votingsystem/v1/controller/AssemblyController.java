package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.service.AssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/assembly/v1")
public class AssemblyController {

    @Autowired
    AssemblyService service;

    @GetMapping
    public ResponseEntity<List<AssemblyDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public AssemblyDTO findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @PostMapping
    public AssemblyDTO create(@RequestBody  AssemblyDTO assemblyDTO) throws Exception {
        return service.create(assemblyDTO);
    }

    @PutMapping
    public AssemblyDTO update(@RequestBody  AssemblyDTO assemblyDTO){
        return service.update(assemblyDTO);
    }
}