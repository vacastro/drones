package com.castro.drones.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Drone;
import com.castro.drones.entities.Shipping;
import com.castro.drones.entities.Itinerary;
import com.castro.drones.entities.Medication;
import com.castro.drones.entities.MedicineDispensed;
import com.castro.drones.enums.ShippingStatus;
import com.castro.drones.enums.Status;
import com.castro.drones.exception.DronException;
import com.castro.drones.exception.ShippingException;
import com.castro.drones.exception.ValidationException;
import com.castro.drones.repository.DroneRepository;
import com.castro.drones.repository.ItineraryRepository;
import com.castro.drones.repository.MedicineDispensedRepository;
import com.castro.drones.repository.ShippingRepository;
import com.castro.drones.response.DroneResponse;
import com.castro.drones.response.ShippingResponse;

@Service
public class ShippingService {
	
	Shipping shipping = null;
	Itinerary itinerary = null;
	Drone dron =null;
	ShippingResponse response = null;
	DroneResponse dronResponse = null;
	Medication medication = null;
	
	@Autowired
	ShippingRepository shippingRepository;
	
	@Autowired
	ItineraryRepository itineraryRepository;
	
	@Autowired
	DroneRepository dronRepository;
	
	@Autowired
	DroneService dronService;
	
	@Autowired
	MedicationService medicationService;
	
	@Autowired
	MedicineDispensedRepository medicineDispensedRepository;
	
	public ShippingResponse createShipping(Shipping shippingData) {
		
		shipping = new Shipping(shippingData.getIdUser(), shippingData.getInvoice(), shippingData.getAdress());
		verifyShipping(shipping);
		
		shippingRepository.save(shipping);
		itinerary = new Itinerary(shipping);
		itineraryRepository.save(itinerary);
		shipping.getListItinerary().add(itinerary);
		response =new ShippingResponse("shipping registered successfully", shipping);
		return response;
	}
	
	public void verifyShipping(Shipping shipping) {
		Optional<Shipping> searchShipping = shippingRepository.findByInvoice(shipping.getInvoice());
		if(searchShipping.isPresent()) {
			throw new ValidationException("there is a shipping with the same invoice number");
		}
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
			throw new ShippingException("there is not a shipping with that invoice number in the database");
		}
		return shipping;
	}
	
	public Shipping findShippingById(long id) {
		
		Optional<Shipping>searchShipping = shippingRepository.findById(id);
		if(searchShipping.isPresent()) {
			shipping = searchShipping.get();
		}else {
			throw new ShippingException("this shipping does not exist in the database");
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
	
	public ShippingResponse loadingShipping (long idShipping) {
		shipping= findShippingById(idShipping);		
		dron = dronService.getDronReadyToUse();
		
		shipping.setDron(dron);
		shippingRepository.assignDevice(dron, shipping.getIdShipping());
		
		itinerary = itineraryGenerator(dron,Status.LOADING.toString(),ShippingStatus.ON_PROCESS.toString());
		shipping.getListItinerary().add(itinerary);
		response = new ShippingResponse("A drone was assigned to the shipping, that is ready to be loaded with medication", shipping);
		return response;
	}
	
	public ShippingResponse loadedShipping(long idShipping) {
		shipping = findShippingById(idShipping);
		dron = shipping.getDron();
		if (shipping.getListMedicine().isEmpty()) {
			throw new ShippingException(
					"the cargo is empty, you must load medication to send the drone"
					+ "");
		} else {
			itinerary = itineraryGenerator(dron, Status.LOADED.toString(), ShippingStatus.PACKED.toString());
			shipping.getListItinerary().add(itinerary);
			response = new ShippingResponse("completed cargo", shipping);
		}
		return response;
	}
	
	public ShippingResponse deliveringShipping(long idShipping) {
		shipping= findShippingById(idShipping);
		dron = shipping.getDron();
		itinerary = itineraryGenerator(dron, Status.DELIVERING.toString(),ShippingStatus.IN_TRANSIT.toString());
		shipping.getListItinerary().add(itinerary);
		response = new ShippingResponse("the shipping was authorized, the drone is in transit"
				+ ""
				+ "", shipping);
		return response;
	}
	
	public ShippingResponse deliveredShipping(long idShipping) {
		shipping= findShippingById(idShipping);
		dron = shipping.getDron();
		itinerary = itineraryGenerator(dron, Status.DELIVERED.toString(),ShippingStatus.DELIVERED.toString());
		shipping.getListItinerary().add(itinerary);
		response = new ShippingResponse("shipping delivered successfully", shipping);
		return response;
	}
	
	public Itinerary itineraryGenerator (Drone dron, String statusDron, String statusItinerary) {
		dron.setStatus(statusDron);
		if(!statusItinerary.equals(ShippingStatus.CANCELED.toString())) {
			verifyShippingStatus(shipping.getListItinerary().get(shipping.getListItinerary().size()-1).getItinerary().getShippingStatus(), statusItinerary);
		}
		itinerary = new Itinerary(shipping);
		itinerary.getItinerary().setShippingStatus(statusItinerary);
		dronRepository.updateDron(dron.getStatus(), dron.getIdDron());
		itineraryRepository.save(itinerary);
		return itinerary;
	}
	
	public void verifyShippingStatus(String actualStatus, String nextStatus) {
		int currentStatus = 0;
		int newStatus = 0;
		ShippingStatus[] listShippingStatus = ShippingStatus.values();
		if (!actualStatus.equals(ShippingStatus.CANCELED.toString())) {
			for (int i = 0; i < listShippingStatus.length; i++) {
				if (listShippingStatus[i].toString().equals(actualStatus)) {
					currentStatus = i;
				}
				if (listShippingStatus[i].toString().equals(nextStatus)) {
					newStatus = i;
				}
			}
		}else {
			throw new ShippingException("the shipping cannot be modified because it is canceled");
		}

		if (!(currentStatus + 1 == newStatus)) {
			throw new ValidationException("this operation is not allowed because de shipping status is: "
					+ actualStatus);
		}
	}
	
	public DroneResponse returningDron(long idShipping) {
		shipping = findShippingById(idShipping);
		String nextStatus = Status.RETURNING.toString();
		if (dron.getStatus().equals(Status.DELIVERED.toString())) {
			dron.setStatus(nextStatus);
			dronRepository.updateDron(nextStatus, dron.getIdDron());
			dronResponse = new DroneResponse("the shipping is delivered, the drone is returning", dron);
		}else {
			throw new DronException("the drone cannnot return because the status is : " + dron.getStatus());
		}
		
		return dronResponse;
	}
	
	public ShippingResponse addMedication(long idShipping, long idMedication) {
		shipping = findShippingById(idShipping);
		medication = medicationService.findMedicationById(idMedication);
		dron = new Drone();
		String shippingStatus =shipping.getListItinerary().get(shipping.getListItinerary().size() - 1).getItinerary().getShippingStatus();
		int weight = shipping.getWeight() + medication.getWeight();
		if (shippingStatus.equals(ShippingStatus.ON_PROCESS.toString())) {
			if (weight < dron.getWEIGHT_LIMIT()) {
				MedicineDispensed medicineAdd = new MedicineDispensed(medication, shipping);
				medicineDispensedRepository.save(medicineAdd);
				shipping.setWeight(weight);
				shipping.getListMedicine().add(medicineAdd);
				shippingRepository.updateWeight(weight, shipping.getIdShipping());
				response = new ShippingResponse("medication added to the shipment", shipping);
			} else {
				throw new ShippingException("can not add this medication, because exceeds the allowed weight");
			}
		}else {
			throw new ShippingException("can not add this medication, because shipping status: "+shippingStatus);
		}
		return response;
	}

	public Shipping findShippingByUser(int idUser, long idShipping) {
		if(shippingRepository.findShippingByUser(idUser, idShipping).isPresent()) {
			shipping = shippingRepository.findShippingByUser(idUser, idShipping).get();
		}else {
			throw new ShippingException("data does not match");
		}	
		return shipping;
	}
	
	public List<Shipping> findAllShippingsByUser(int idUser) {
		List<Shipping> allShippings = new ArrayList<Shipping>();
		if(shippingRepository.findAllShippingByUser(idUser).isPresent()) {
			allShippings = shippingRepository.findAllShippingByUser(idUser).get();
			
		}
		
		if(allShippings.isEmpty()) {
			throw new ShippingException("user does not have shipping");
		}else {
			return allShippings;
		}
	}
	
	public ShippingResponse cancelShipping(long idShipping) {
		shipping = findShippingById(idShipping);
		dron = shipping.getDron();
		String actualStatus = shipping.getListItinerary().get(shipping.getListItinerary().size() - 1).getItinerary()
				.getShippingStatus();
		if(dron ==null) {
			itinerary = new Itinerary(shipping);
			itinerary.getItinerary().setShippingStatus(ShippingStatus.CANCELED.toString());
			itineraryRepository.save(itinerary);
			shipping.getListItinerary().add(itinerary);
			response = new ShippingResponse("shipping has been canceled successfully", shipping);
		}else if (actualStatus.equals(ShippingStatus.ON_PROCESS.toString())
				|| actualStatus.equals(ShippingStatus.PACKED.toString())) {
			itinerary = itineraryGenerator(dron, Status.IDLE.toString(), ShippingStatus.CANCELED.toString());
			shipping.getListItinerary().add(itinerary);
			response = new ShippingResponse("shipping has been canceled successfully", shipping);
		}else {
			throw new ShippingException("shipping can not be canceled because, his status is : "+ actualStatus);
		}
		return response;
	}

}
