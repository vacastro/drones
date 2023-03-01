package com.castro.drones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.Itinerary;
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
	public ShippingResponse newShipping (@RequestBody Shipping shippingData) throws Exception{
		return shippingService.createShipping(shippingData);
	}
	
	@GetMapping("/all-shippings")
	public List<Shipping> getAllShipping(){
		return shippingService.findAllShippings();
	}
	
	@GetMapping("/find-shipping/{idShipping}")
	public Shipping findShipping(@PathVariable("idShipping") long idShipping){
		return shippingService.findShippingById(idShipping);
	}
	
	@GetMapping("/find-shipping-by-invoice/{invoice}")
	public Shipping findShippingByInvoice(@PathVariable("invoice") int invoice){
		return shippingService.findShippingById(invoice);
	}
	
	@GetMapping("/all-itineraries")
	public List<Itinerary> getAllItineraries(){
		return shippingService.findAllItineraries();
	}
	
	@PostMapping (value = "/loading-shipping")
	public ShippingResponse loadingShipping (@RequestBody Shipping shippingData) throws Exception{
		return shippingService.loadingShipping(shippingData);
	}
	
	@PostMapping (value = "/loaded-shipping")
	public ShippingResponse loadedShipping (@RequestBody ShippingData shippingData) throws Exception{
		return shippingService.loadedShipping(shippingData);
	}
	
	
	

}
