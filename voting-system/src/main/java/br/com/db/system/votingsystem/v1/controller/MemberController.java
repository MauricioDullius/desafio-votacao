package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member/v1")
public class MemberController {

    @Autowired
    private MemberService service;

    @GetMapping
    public ResponseEntity<List<MemberDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MemberDTO> create(@RequestBody MemberDTO memberDTO) throws Exception {
        MemberDTO created = service.create(memberDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping
    public ResponseEntity<MemberDTO> update(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(service.update(memberDTO));
    }

    @GetMapping("/findMemberByCpf/{cpf}")
    public ResponseEntity<MemberDTO> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByCpf(@RequestParam String cpf) {
        service.deleteByCpf(cpf);
        return ResponseEntity.noContent().build();
    }
}