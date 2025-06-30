package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.service.AgendaService;
import br.com.db.system.votingsystem.v1.service.AssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/agenda/v1")
public class AgendaController {

    @Autowired
    AgendaService service;

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public AgendaDTO findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @PostMapping
    public AgendaDTO create(@RequestBody  AgendaDTO agendaDTO) throws Exception {
        return service.create(agendaDTO);
    }

    @PutMapping
    public AgendaDTO update(@RequestBody  AgendaDTO agendaDTO) throws Exception {
        return service.update(agendaDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id){
        service.deleteById(id);
    }

}
