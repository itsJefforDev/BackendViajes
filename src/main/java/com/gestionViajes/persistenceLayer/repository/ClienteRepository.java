package com.gestionViajes.persistenceLayer.repository;

import com.gestionViajes.persistenceLayer.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
