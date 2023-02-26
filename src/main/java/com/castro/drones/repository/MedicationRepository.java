package com.castro.drones.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.castro.drones.entities.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>{
	
	@Query (value= "SELECT u FROM Medication u WHERE u.code =?1 ")
	Optional<Medication> findByCode(String code);

}
