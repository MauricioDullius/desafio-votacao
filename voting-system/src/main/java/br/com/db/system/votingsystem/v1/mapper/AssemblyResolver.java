package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.model.Assembly;

public interface AssemblyResolver {
    Assembly resolve(Long id);
}