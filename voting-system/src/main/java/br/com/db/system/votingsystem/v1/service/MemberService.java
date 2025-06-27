package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import static br.com.db.system.votingsystem.v1.mapper.ObjectMapper.parseObject;
import br.com.db.system.votingsystem.v1.model.Member;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberRepository repository;

    public List<Member> findAll(){

        return repository.findAll();
    }

    public MemberDTO findById(Long id){
        Member member = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return parseObject(member,MemberDTO.class);
    }

    public MemberDTO create(MemberDTO memberDTO) throws Exception {
        Member member = parseObject(memberDTO, Member.class);

        if( repository.findMemberByCpf(member.getCpf()) != null )
            throw new Exception("Member with id: " + member.getCpf() + " already exists");
        return parseObject(repository.save(member), MemberDTO.class);
    }

    public MemberDTO update(MemberDTO memberDTO){

        Member member = repository.findById(memberDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberDTO.getId()));
        member.setName(memberDTO.getName());
        member.setCpf(memberDTO.getCpf());
        member.setActive(memberDTO.isActive());
        return parseObject(repository.save(member), MemberDTO.class);
    }

    public void delete(MemberDTO memberDTO){
        repository.delete(parseObject(memberDTO, Member.class));
    }

    public MemberDTO findByCpf(String cpf){
        repository.findMemberByCpf(cpf);
        return parseObject(repository.findMemberByCpf(cpf), MemberDTO.class);
    }

    public void deleteByCpf(String cpf){
        Member member = repository.findMemberByCpf(cpf);
        repository.delete(member);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }
}