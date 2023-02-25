package com.castro.drones.service;

import java.sql.Timestamp;
import java.util.List;

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
	
	//TO DO
	public int checkBatteryStatus() {
		return dron.getBatteryCapacity();
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
		
		boolean serialNum= dronData.getSerialNumber().trim().equals(StringUtils.EMPTY);
		boolean model = dronData.getDronModel().trim().equals(StringUtils.EMPTY);
		
		DronModel dModel= null;
		String serialN="";
		
		if (serialNum) {
			throw new ValidationException("Serial Number can not be empty");
		}
		
		if (model) {
			throw new ValidationException("Dron Model can not be empty");
		}
		
		for (DronModel dronModel : DronModel.values()) {
			if (dronModel.toString().equals(dronData.getDronModel().trim().toUpperCase())) {
				dModel = dronModel;
			}
		}
		
		if(dModel == null) {
			throw new ValidationException("that model does not exist for this type of device");
		}
		
		
		if(dronData.getSerialNumber().length()>100) {
			throw new ValidationException("the serial number exceeds the number of allowed digits");
		} else {
			
			serialN =dronData.getSerialNumber().toUpperCase();
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
	
	public List<Dron> getIdleDrones() {
		

		List<Dron> dronesList = null;
				
		dronesList=dronRepository.findByIdleDron();

		if (dronesList.size() ==0) {
			throw new DronException("no devices available");
		}

		return dronesList;
	}


}
