package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vote/v1")
public class VoteController {

    @Autowired
    private VoteService service;

    @GetMapping
    public ResponseEntity<List<VoteDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public VoteDTO findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @PostMapping
    public VoteDTO create(@RequestBody  VoteDTO voteDTO) throws Exception {
        return service.create(voteDTO);
    }

    @PutMapping
    public VoteDTO update(@RequestBody  VoteDTO voteDTO) throws Exception {
        return service.update(voteDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        service.deleteById(id);
    }
}