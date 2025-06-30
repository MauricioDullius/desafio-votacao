package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.Agenda;
import br.com.db.system.votingsystem.v1.model.Assembly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssemblyRepository extends JpaRepository<Assembly, Long> {}
