package com.gestionViajes.persistenceLayer.repository;

import com.gestionViajes.persistenceLayer.entity.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservasRepository extends JpaRepository<Reservas, Long> {

}
