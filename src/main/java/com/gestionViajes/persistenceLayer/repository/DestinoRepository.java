package com.gestionViajes.persistenceLayer.repository;

import com.gestionViajes.persistenceLayer.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Long> {

}