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
import com.castro.drones.entities.MedicineDispensed;
import com.castro.drones.response.DroneResponse;
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
	
	@GetMapping("/shipping-by-user/{idUser}/{idShipping}")
	public Shipping findShippingsByUser(@PathVariable("idUser") int idUser, @PathVariable("idShipping") long idShipping){
		return shippingService.findShippingByUser(idUser,idShipping);
	}
	
	@GetMapping("/shippings-by-user/{idUser}")
	public List<Shipping> findAllShippingsByUser(@PathVariable("idUser") int idUser){
		return shippingService.findAllShippingsByUser(idUser);
	}
	
	@PostMapping (value = "/loading-shipping")
	public ShippingResponse loadingShipping (@RequestBody Shipping shipping) throws Exception{
		return shippingService.loadingShipping(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/loaded-shipping")
	public ShippingResponse loadedShipping (@RequestBody Shipping shipping) throws Exception{
		return shippingService.loadedShipping(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/delivering-shipping")
	public ShippingResponse deliveringShipping (@RequestBody Shipping shipping) throws Exception{
		return shippingService.deliveringShipping(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/delivered-shipping")
	public ShippingResponse deliveredShipping (@RequestBody Shipping shipping) throws Exception{
		return shippingService.deliveredShipping(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/returning-dron")
	public DroneResponse returningDron (@RequestBody Shipping shipping) throws Exception{
		return shippingService.returningDron(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/cancel-shipping")
	public ShippingResponse cancelShipping (@RequestBody Shipping shipping) throws Exception{
		return shippingService.cancelShipping(shipping.getIdShipping());
	}
	
	@PostMapping (value = "/add-medication")
	public ShippingResponse addMedication (@RequestBody MedicineDispensed medicineDispensed) throws Exception{
		return shippingService.addMedication(medicineDispensed.getShipping().getIdShipping(), medicineDispensed.getMedication().getIdMedication());
	}
	
}
