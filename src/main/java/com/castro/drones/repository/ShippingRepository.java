package com.castro.drones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.castro.drones.entities.Drone;
import com.castro.drones.entities.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long>{
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Shipping u SET u.dron = ?1 WHERE u.idShipping = ?2")
	void assignDevice(Drone Dron, long idShipping );

	@Query(value = "SELECT u FROM Shipping u WHERE u.invoice =?1")
	Optional<Shipping> findByInvoice(int invoice);
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Shipping u SET u.weight = ?1 WHERE u.idShipping = ?2")
	void updateWeight(int weight, long idShipping );
	
	@Query(value = "SELECT u FROM Shipping u WHERE u.idUser =?1 AND u.idShipping =?2")
	Optional<Shipping> findShippingByUser(int idUser, long idShipping);
	
	@Query(value = "SELECT u FROM Shipping u WHERE u.idUser =?1")
	Optional<List<Shipping>>findAllShippingByUser(int idUser);
}
