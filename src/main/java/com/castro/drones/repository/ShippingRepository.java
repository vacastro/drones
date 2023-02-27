package com.castro.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.castro.drones.entities.Dron;
import com.castro.drones.entities.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long>{
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Shipping u SET u.dron = ?1 WHERE u.idShipping = ?2")
	void assignDevice(Dron Dron, long idShipping );
	
}
