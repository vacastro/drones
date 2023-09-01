package com.castro.drones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.castro.drones.entities.Drone;
import com.castro.drones.enums.Status;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long>{
	
	@Query(value= "SELECT u FROM Drone u WHERE u.status = 'IDLE'")
	List<Drone> findByIdleDron ();
	
	@Query(value="SELECT u FROM Drone u WHERE u.serialNumber =?1 ")
	Optional<Drone> findBySerialNumber(String serialNumber);
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Drone u SET u.status = ?1 WHERE u.idDron = ?2")
	void updateDron(Status status, long idDron );
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Drone u SET u.batteryCapacity = ?1 WHERE u.idDron =?2")
	void updateBattery(int battery, long idDron);

}
