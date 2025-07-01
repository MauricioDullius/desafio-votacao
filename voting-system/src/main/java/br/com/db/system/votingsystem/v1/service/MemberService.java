package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.mapper.MemberMapper;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberMapper mapper;

    public List<MemberDTO> findAll() {
        return mapper.toDTOList(repository.findAll());
    }

    public MemberDTO findById(Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return mapper.toDTO(member);
    }

    public MemberDTO findByCpf(String cpf) {
        Member member = repository.findMemberByCpf(cpf);
        if (member == null) {
            throw new EntityNotFoundException("Member not found with CPF: " + cpf);
        }
        return mapper.toDTO(member);
    }

    public MemberDTO create(MemberDTO memberDTO) throws Exception {
        if (repository.findMemberByCpf(memberDTO.getCpf()) != null) {
            throw new Exception("Member with CPF: " + memberDTO.getCpf() + " already exists");
        }

        Member member = mapper.toEntity(memberDTO);
        return mapper.toDTO(repository.save(member));
    }

    public MemberDTO update(MemberDTO memberDTO) {
        Member member = repository.findById(memberDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberDTO.getId()));

        member.setName(memberDTO.getName());
        member.setCpf(memberDTO.getCpf());
        member.setActive(memberDTO.isActive());

        return mapper.toDTO(repository.save(member));
    }

    public void delete(MemberDTO memberDTO) {
        Member member = mapper.toEntity(memberDTO);
        repository.delete(member);
    }

    public void deleteByCpf(String cpf) {
        Member member = repository.findMemberByCpf(cpf);
        if (member != null) {
            repository.delete(member);
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}