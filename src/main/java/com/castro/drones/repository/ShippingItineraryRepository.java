package com.castro.drones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.castro.drones.entities.ShippingItinerary;

@Repository
public interface ShippingItineraryRepository extends JpaRepository<ShippingItinerary, Long> {
	
	//TODO agregar order by date desc --> ver bien lo de la primary key
	@Query(value= "SELECT u FROM ShippingItinerary u WHERE u.shipping =?1")
	List<ShippingItinerary> getItinerayByShipping(long idShipping);

}
