package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.client.FakeCpfValidatorClient;
import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.client.dto.ValidationResult;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.MemberMapper;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberMapper mapper;

    @Autowired
    private FakeCpfValidatorClient cpfValidator;

    public Page<MemberDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all members");
        Page<Member> membersPage = repository.findAll(pageable);
        logger.info("Found {} members", membersPage.getNumberOfElements());
        return membersPage.map(mapper::toDTO);
    }

    public MemberDTO findById(Long id) {
        logger.info("Searching member by id {}", id);
        Member member = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Member not found with id {}", id);
                    return new ResourceNotFoundException("Member not found with id: " + id);
                });
        logger.info("Member found with id {}", id);
        return mapper.toDTO(member);
    }

    public MemberDTO findByCpf(String cpf) {
        logger.info("Searching member by CPF {}", cpf);
        Member member = repository.findMemberByCpf(cpf);
        if (member == null) {
            logger.error("Member not found with CPF {}", cpf);
            throw new ResourceNotFoundException("Member not found with CPF: " + cpf);
        }
        logger.info("Member found with CPF {}", cpf);
        return mapper.toDTO(member);
    }

    public MemberDTO create(MemberDTO memberDTO) {
        logger.info("Creating member with CPF {}", memberDTO.getCpf());
        validateMemberDTO(memberDTO);

        if (repository.findMemberByCpf(memberDTO.getCpf()) != null) {
            logger.error("Member with CPF {} already exists", memberDTO.getCpf());
            throw new BusinessRuleException("Member with CPF: " + memberDTO.getCpf() + " already exists");
        }

        ValidationResult result = cpfValidator.validateCpf(memberDTO.getCpf());

        if (!result.isValid()) {
            logger.warn("CPF {} is invalid", memberDTO.getCpf());
            throw new ResourceNotFoundException("Invalid CPF: " + memberDTO.getCpf());
        }

        Member member = mapper.toEntity(memberDTO);
        Member saved = repository.save(member);
        logger.info("Member created successfully with id {}", saved.getId());
        return mapper.toDTO(saved);
    }

    public MemberDTO update(MemberDTO memberDTO) {
        logger.info("Updating member with id {}", memberDTO.getId());
        validateMemberDTO(memberDTO);

        Member existing = repository.findById(memberDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Member not found with id {}", memberDTO.getId());
                    return new ResourceNotFoundException("Member not found with id: " + memberDTO.getId());
                });

        ValidationResult result = cpfValidator.validateCpf(memberDTO.getCpf());

        if (!result.isValid()) {
            logger.warn("CPF {} is invalid", memberDTO.getCpf());
            throw new ResourceNotFoundException("Invalid CPF: " + memberDTO.getCpf());
        }

        existing.setName(memberDTO.getName());
        existing.setCpf(memberDTO.getCpf());
        existing.setActive(memberDTO.isActive());

        Member updated = repository.save(existing);
        logger.info("Member updated successfully with id {}", updated.getId());
        return mapper.toDTO(updated);
    }

    public void deleteByCpf(String cpf) {
        logger.info("Deleting member by CPF {}", cpf);
        Member member = repository.findMemberByCpf(cpf);
        if (member == null) {
            logger.error("Member not found with CPF {}", cpf);
            throw new ResourceNotFoundException("Member not found with CPF: " + cpf);
        }
        repository.delete(member);
        logger.info("Member deleted with CPF {}", cpf);
    }

    private void validateMemberDTO(MemberDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            logger.warn("Invalid request: Name is null or blank");
            throw new InvalidRequestException("Name must not be null or blank");
        }
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            logger.warn("Invalid request: CPF is null or blank");
            throw new InvalidRequestException("CPF must not be null or blank");
        }
    }
}