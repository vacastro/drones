package com.castro.drones.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Dron;
import com.castro.drones.enums.DronModel;
import com.castro.drones.exception.DronException;
import com.castro.drones.exception.RegisterException;
import com.castro.drones.exception.ValidationException;
import com.castro.drones.model.DronData;
import com.castro.drones.repository.DronRepository;
import com.castro.drones.response.DronResponse;

@Service
public class DronService {
	
	Dron dron = null;
	
	@Autowired
	DronRepository dronRepository;
	
	public DronResponse checkBatteryStatus(long idDron) {
		DronResponse response = new DronResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		int dronBattery = 0;
		
		Optional<Dron> dronN = dronRepository.findById(idDron);
		
		if(dronN.isPresent()) {
			dron= dronN.get();
		}else {
			throw new DronException("wrong device number, the device number is not found in the database");
		}
		
		dronBattery = dron.getBatteryCapacity();
	
		if(dronBattery <25) {
			throw new DronException("Insufficient battery: " + dronBattery +"%");
		}else {
			response.setSuccess(Boolean.TRUE);
			response.setResultString("device with enough battery: " + dronBattery +"%" );
			response.setDron(dron);
		}
		
		return response;
	}
	public DronResponse createDron(DronData dronData) throws Exception{

		dron = validationDron(dronData);

		if (dronRepository.findAll().size() == 10) {
			throw new RegisterException("the fleet is already fully registered");
		} else if (dronRepository.findBySerialNumber(dron.getSerialNumber()).isPresent()) {
			throw new RegisterException("the device is already been registered");
		} else {
			return register(dron);
		}
	}
	
	public Dron validationDron(DronData dronData) {
		
		boolean serialNum = dronData.getSerialNumber().trim().equals(StringUtils.EMPTY);
		boolean model = dronData.getDronModel().trim().equals(StringUtils.EMPTY);

		String dModel = null;
		String serialN = "";

		if (serialNum) {
			throw new ValidationException("Serial Number can not be empty");
		} else if (dronData.getSerialNumber().trim().length() > 100) {
			throw new ValidationException("the serial number exceeds the number of allowed digits");
		} else {
			serialN = dronData.getSerialNumber().trim();
		}

		if (model) {
			throw new ValidationException("Dron Model can not be empty");
		} else {
			for (DronModel dronModel : DronModel.values()) {
				if (dronModel.toString().equals(dronData.getDronModel().trim().toUpperCase())) {
					dModel = dronModel.toString();
				}
			}
		}
		
		if(dModel == null) {
			throw new ValidationException("that model does not exist for this type of device");
		}
		
		dron = new Dron(serialN, dModel);
		return dron;
	}
	
	
	public DronResponse register(Dron dron) {
		
		DronResponse response = new DronResponse();
		response.setTimestamp(new Timestamp(System.currentTimeMillis()));
		dronRepository.save(dron);
		response.setSuccess(Boolean.TRUE);
		response.setResultString("device registered successfully");
		response.setDron(dron);
		
		return response;
	}
	
	public List<Dron> findAllDrones(){
		
		List<Dron> dronesList = null;
		dronesList=dronRepository.findAll();
		
		if (dronesList.size() == 0) {
			throw new DronException("no device has been registered yet");	
		}
		
		return dronesList;
	}
	
	public List<Dron> getAvailableDrones() {
		
		List<Dron> dronesList = null;
				
		dronesList=dronRepository.findByIdleDron();

		if (dronesList.size() ==0) {
			throw new DronException("no devices available");
		}

		return dronesList;
	}
	
	public Dron getDronReadyToUse() {
		
		List<Dron> dronesList = getAvailableDrones();
		
		List<Dron> listDronesReady = new ArrayList<Dron>();
		
		for(Dron dronN: dronesList) {
			if (dronN.getBatteryCapacity() >25) {
				listDronesReady.add(dronN);
			}
		}
		
		if(listDronesReady.size() ==0) {
			throw new DronException("no devices available to use");
		}
		
		return listDronesReady.get(0);
	}


}
