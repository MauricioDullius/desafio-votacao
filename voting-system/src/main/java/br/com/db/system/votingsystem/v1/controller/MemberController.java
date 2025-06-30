package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.mapper.MemberMapper;
import br.com.db.system.votingsystem.v1.model.Member;
import br.com.db.system.votingsystem.v1.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/member/v1")
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired

    @GetMapping
    public ResponseEntity<List<MemberDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public MemberDTO findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @PostMapping
    public MemberDTO create(@RequestBody  MemberDTO memberDTO) throws Exception {
        return service.create(memberDTO);
    }

    @PutMapping
    public MemberDTO update(@RequestBody  MemberDTO memberDTO){
        return service.update(memberDTO);
    }

    @GetMapping(value = "findMemberByCpf/{cpf}")
    public MemberDTO findByCpf(@PathVariable(value = "cpf") String cpf){
        return service.findByCpf(cpf);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id){
        service.deleteById(id);
    }

    @DeleteMapping
    public void deleteByCpf(@RequestParam String cpf){
        service.deleteByCpf(cpf);
    }

}
