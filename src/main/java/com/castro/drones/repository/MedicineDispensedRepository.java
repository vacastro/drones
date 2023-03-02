package com.castro.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.castro.drones.entities.MedicineDispensed;

public interface MedicineDispensedRepository extends JpaRepository<MedicineDispensed, Long>{

}
