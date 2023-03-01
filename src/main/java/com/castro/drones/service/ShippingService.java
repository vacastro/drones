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
import com.castro.drones.exception.ValidationException;
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
	ShippingResponse response = null;
	
	@Autowired
	ShippingRepository shippingRepository;
	
	@Autowired
	ItineraryRepository itineraryRepository;
	
	@Autowired
	DronRepository dronRepository;
	
	@Autowired
	DronService dronService;
	
	public ShippingResponse createShipping(Shipping shippingData) {
		
		shipping = new Shipping(shippingData.getIdUser(), shippingData.getInvoice(), shippingData.getAdress());
		verifyShipping(shipping);
		
		shippingRepository.save(shipping);
		itinerary = new Itinerary(shipping);
		itineraryRepository.save(itinerary);
		
		response =new ShippingResponse("shipping registered successfully", shipping);
		return response;
	}
	
	public List<Shipping> findAllShippings() {
		List<Shipping> listShipping = null;
		listShipping = shippingRepository.findAll();
		
		if (listShipping.size() ==0) {
			throw new ShippingException("no shipping orders registered yet");
		} 
		return listShipping;
	}
	
	public Shipping findShippingByInvoice(int invoice) {
		Optional<Shipping> searchShipping = shippingRepository.findByInvoice(invoice);
		if(searchShipping.isPresent()) {
			shipping = searchShipping.get();
		}else {
			throw new ShippingException("no se localizo el shipping");
		}
		return shipping;
	}
	
	public void verifyShipping(Shipping shipping) {
		Optional<Shipping> searchShipping = shippingRepository.findByInvoice(shipping.getInvoice());
		if(searchShipping.isPresent()) {
			throw new ValidationException("existe un shipping con igual numero de invoice");
		}
	}
	
	public Shipping findShippingById(long id) {
		
		Optional<Shipping>searchShipping = shippingRepository.findById(id);
		if(searchShipping.isPresent()) {
			shipping = searchShipping.get();
		}else {
			throw new ShippingException("no se localizo el shipping");
		}	
		return shipping;
	}
	
	public List<Itinerary> findAllItineraries() {
		List<Itinerary> listItineraries =null;
		listItineraries = itineraryRepository.findAll();
		
		if(listItineraries.size() ==0) {
			throw new ShippingException("no itineraries registered yet");
		}		
		return listItineraries;
	}
	
	
	public ShippingResponse loadingShipping (Shipping shippingData) {
		shipping= findShippingById(shippingData.getIdShipping());
		
		dron = dronService.getDronReadyToUse();
	
		shipping.setDron(dron);
		shippingRepository.assignDevice(dron, shipping.getIdShipping());

		dron.setStatus(Status.LOADING.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itinerary = new Itinerary(shipping);
		itinerary.getPk().setShippingStatus(ShippingStatus.ON_PROCESS.toString());
		itineraryRepository.save(itinerary);

		response = new ShippingResponse("el envio esta listo para ser cargado, se asigno un dron", shipping);
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
	
	
	//TODO evitar que un shipping quede en estado de finalizado cuando no tiene productos agregados
	public ShippingResponse loadedShipping(ShippingData shippingData) {
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		dron = shipping.getDron();

		dron.setStatus(Status.LOADED.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itinerary = new Itinerary(shipping);
		itinerary.getPk().setShippingStatus(ShippingStatus.PACKED.toString());
		itineraryRepository.save(itinerary);

		response.setSuccess(Boolean.TRUE);
		response.setResultString("the shipment is ready");
		response.setShipping(shipping);
//		response.getShipping().setListShippingItinerary(shippingItineraryRepository.getItinerayByShipping(shipping.getIdShipping()));;
		return response;
	}
	
	public ShippingResponse deliveringShipping(ShippingData shippingData) {
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		dron = shipping.getDron();

		dron.setStatus(Status.DELIVERING.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itinerary = new Itinerary(shipping);
		itinerary.getPk().setShippingStatus(ShippingStatus.IN_TRANSIT.toString());
		itineraryRepository.save(itinerary);

		response.setSuccess(Boolean.TRUE);
		response.setResultString("the shipment is in transit");
		response.setShipping(shipping);
//		response.getShipping().setListShippingItinerary(shippingItineraryRepository.getItinerayByShipping(shipping.getIdShipping()));;
		return response;
	}
	
	public ShippingResponse deliveredShipping(ShippingData shippingData) {
		ShippingResponse response = new ShippingResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		dron = shipping.getDron();

		dron.setStatus(Status.DELIVERED.toString());
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itinerary = new Itinerary(shipping);
		itinerary.getPk().setShippingStatus(ShippingStatus.DELIVERED.toString());
		itineraryRepository.save(itinerary);

		response.setSuccess(Boolean.TRUE);
		response.setResultString("the shipment is in delivered");
		response.setShipping(shipping);
//		response.getShipping().setListShippingItinerary(shippingItineraryRepository.getItinerayByShipping(shipping.getIdShipping()));;
		return response;
	}

}
