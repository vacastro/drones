package com.castro.drones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.ShippingItinerary;
import com.castro.drones.entities.ShippingItineraryPK;

@Repository
public interface ShippingItineraryRepository extends JpaRepository<ShippingItinerary, ShippingItineraryPK> {
	
	@Query(value= "SELECT u FROM ShippingItinerary u WHERE u.shipping =?1 ORDER BY u.pk.date DESC")
	List<ShippingItinerary> getItinerayByShipping(long idShipping);

}
