package com.castro.drones.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Dron;
import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.Itinerary;
import com.castro.drones.enums.ShippingStatus;
import com.castro.drones.enums.Status;
import com.castro.drones.exception.ShippingException;
import com.castro.drones.model.ShippingData;
import com.castro.drones.repository.DronRepository;
import com.castro.drones.repository.ItineraryRepository;
import com.castro.drones.repository.ShippingRepository;
import com.castro.drones.response.ShippingResponse;

@Service
public class ShippingService {
	
	Shipping shipping = null;
	Itinerary itinerary = null;
	Dron dron =null;
	
	@Autowired
	ShippingRepository shippingRepository;
	
	@Autowired
	ItineraryRepository itineraryRepository;
	
	@Autowired
	DronRepository dronRepository;
	
	@Autowired
	DronService dronService;
	
	public ShippingResponse createShipping(ShippingData shippingData) {
		
		shipping = new Shipping(shippingData.getIdUser(), shippingData.getInvoice(), shippingData.getAdress());
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		shippingRepository.save(shipping);
		itinerary = new Itinerary(shipping);
		itineraryRepository.save(itinerary);
		response.setSuccess(Boolean.TRUE);
		response.setResultString("shipping registered successfully");
		response.setShipping(shipping);

		return response;
	}
	
	//TODO revisar funcion (el controller devuelve errores)
	public List<Shipping> findAllShippings() {
		List<Shipping> listShipping = null;
		listShipping = shippingRepository.findAll();
		
		if (listShipping.size() ==0) {
			throw new ShippingException("no shipping orders registered yet");
		} 
		
		return listShipping;
	}
	
	//TODO revisar funcion (el controller devuelve errores)
	public List<Itinerary> findAllItineraries() {
		List<Itinerary> listItineraries =null;
		listItineraries = itineraryRepository.findAll();
		
		if(listItineraries.size() ==0) {
			throw new ShippingException("no itineraries registered yet");
		}
		
		return listItineraries;
	}
	
	
	public ShippingResponse loadingShipping (ShippingData shippingData) {
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		dron = dronService.getDronReadyToUse();
	
		shipping.setDron(dron);
		shippingRepository.assignDevice(dron, shipping.getIdShipping());

		dron.setStatus(Status.LOADING.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itinerary = new Itinerary(shipping);
		itinerary.getPk().setShippingStatus(ShippingStatus.ON_PROCESS.toString());
		itineraryRepository.save(itinerary);

		response.setSuccess(Boolean.TRUE);
		response.setResultString("the shipment is ready to be loaded");
		response.setShipping(shipping);
//		response.getShipping().setListShippingItinerary(shippingItineraryRepository.getItinerayByShipping(shipping.getIdShipping()));;
		return response;
	}
	
	
//	public Shipping verifyShipping(long idShipping) {
//		
//		Optional<Shipping> shippingO = shippingRepository.findById(idShipping);
//		if (shippingO.isPresent()) {
//			shipping = shippingO.get();
//		}else {
//			throw new ShippingException("shipping number incorrect or no registered in database");
//		}
//		
//		List<ShippingItinerary> itineraries = shippingItineraryRepository.getItinerayByShipping(idShipping);
//		
//		if(itineraries.get(0).getPk().getShippingStatus().equals(ShippingStatus.ORDERED.toString())) {
//			return shipping;
//		}else {
//			throw new ShippingException("esta entrega no se puede poner a cargar porque su estado actual es " + itineraries.get(0).getPk().getShippingStatus());
//		}
//
//	}

}
