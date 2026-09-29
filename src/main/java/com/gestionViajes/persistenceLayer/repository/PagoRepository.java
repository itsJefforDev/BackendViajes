package com.gestionViajes.persistenceLayer.repository;

import com.gestionViajes.persistenceLayer.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}
