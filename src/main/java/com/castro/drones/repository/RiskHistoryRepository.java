package com.castro.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.castro.drones.entities.RiskHistory;

@Repository
public interface RiskHistoryRepository extends JpaRepository<RiskHistory, Long> {

}
