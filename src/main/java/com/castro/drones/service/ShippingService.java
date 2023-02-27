package com.castro.drones.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Dron;
import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.ShippingItinerary;
import com.castro.drones.enums.ShippingStatus;
import com.castro.drones.enums.Status;
import com.castro.drones.exception.ShippingException;
import com.castro.drones.model.ShippingData;
import com.castro.drones.repository.DronRepository;
import com.castro.drones.repository.ShippingItineraryRepository;
import com.castro.drones.repository.ShippingRepository;
import com.castro.drones.response.ShippingResponse;

@Service
public class ShippingService {
	
	Shipping shipping = null;
	ShippingItinerary shippingItinerary = null;
	Dron dron =null;
	
	@Autowired
	ShippingRepository shippingRepository;
	
	@Autowired
	ShippingItineraryRepository shippingItineraryRepository;
	
	@Autowired
	DronRepository dronRepository;
	
	@Autowired
	DronService dronService;
	
	public ShippingResponse createShipping(ShippingData shippingData) {
		
		shipping = new Shipping(shippingData.getIdUser(), shippingData.getInvoice(), shippingData.getAdress());
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		shippingRepository.save(shipping);
		shippingItinerary = new ShippingItinerary(shipping);
		shippingItineraryRepository.save(shippingItinerary);
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
	public List<ShippingItinerary> findAllItineraries() {
		List<ShippingItinerary> listItineraries =null;
		listItineraries = shippingItineraryRepository.findAll();
		
		if(listItineraries.size() ==0) {
			throw new ShippingException("no itineraries registered yet");
		}
		
		return listItineraries;
	}
	
	
	public ShippingResponse loadingShipping (long shippingId) {
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		shipping = verifyShipping(shippingId);
		
		dron = dronService.getDronReadyToUse();
		
		shipping.setDron(dron);
		shippingRepository.assignDevice(dron, shipping.getIdShipping());
		
		dron.setStatus(Status.LOADING.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		shippingItinerary = new ShippingItinerary(shipping);
		shippingItinerary.getShippingItineraryPK().setShippingStatus(ShippingStatus.ON_PROCESS.toString());
		shippingItineraryRepository.save(shippingItinerary);

		response.setSuccess(Boolean.TRUE);
		response.setResultString("the shipment is ready to be loaded");
		response.setShipping(shipping);
		return response;
	}
	
	
	public Shipping verifyShipping(long idShipping) {
		
		Optional<Shipping> shippingO = shippingRepository.findById(idShipping);
		if (shippingO.isPresent()) {
			shipping = shippingO.get();
		}else {
			throw new ShippingException("shipping number incorrect or no registered in database");
		}
		
		List<ShippingItinerary> itineraries = shippingItineraryRepository.getItinerayByShipping(idShipping);
		
		if(itineraries.get(0).getShippingItineraryPK().getShippingStatus().equals(ShippingStatus.ORDERED.toString())) {
			return shipping;
		}else {
			throw new ShippingException("esta entrega no se puede poner a cargar porque su estado actual es " + itineraries.get(0).getShippingItineraryPK().getShippingStatus());
		}

	}

}
