package com.castro.drones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.ShippingItinerary;
import com.castro.drones.model.ShippingData;
import com.castro.drones.response.ShippingResponse;
import com.castro.drones.service.ShippingService;

@RestController
@RequestMapping("/DRON-APP")
public class ShippingController{
	
	@Autowired
	ShippingService shippingService;
	
	@PostMapping (value = "/shipping-register")
	@ResponseStatus(HttpStatus.CREATED)
	public ShippingResponse newShipping (@RequestBody ShippingData shippingData) throws Exception{
		return shippingService.createShipping(shippingData);
	}
	
	//TODO el controller no trae la lista - error serializable
	@GetMapping("/all-shippings")
	public List<Shipping> getAllShipping(){
		return shippingService.findAllShippings();
	}
	
	//TODO el controller no trae la lista - error serializable
	@GetMapping("/all-itineraries")
	public List<ShippingItinerary> getAllItineraries(){
		return shippingService.findAllItineraries();
	}
	
	@PostMapping (value = "/confirm-shipping")
	public ShippingResponse confirmShipping (@RequestBody ShippingData shippingData) throws Exception{
		return shippingService.loadingShipping(shippingData);
	}
	
	

}
