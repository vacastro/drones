package com.castro.drones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.Itinerary;
import com.castro.drones.entities.ItineraryPK;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, ItineraryPK> {
	
	@Query(value= "SELECT u FROM Itinerary u WHERE u.shipping =?1 ORDER BY u.pk.date DESC")
	List<Itinerary> getItinerayByShipping(long idShipping);

}
