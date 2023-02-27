package com.castro.drones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.castro.drones.entities.Dron;

@Repository
public interface DronRepository extends JpaRepository<Dron, Long>{
	
	@Query(value= "SELECT u FROM Dron u WHERE u.status = 'IDLE'")
	List<Dron> findByIdleDron ();
	
	@Query(value="SELECT u FROM Dron u WHERE u.serialNumber =?1 ")
	Optional<Dron> findBySerialNumber(String serialNumber);
	
	@Transactional
	@Modifying()
	@Query(value = "UPDATE Dron u SET u.status = ?1 WHERE u.idDron = ?2")
	void updateDron(String status, long idDron );

}
