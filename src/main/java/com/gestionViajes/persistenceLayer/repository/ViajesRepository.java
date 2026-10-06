package com.gestionViajes.persistenceLayer.repository;

import com.gestionViajes.persistenceLayer.entity.Viajes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajesRepository extends JpaRepository<Viajes, Long> {
}