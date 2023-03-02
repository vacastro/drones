package com.castro.drones.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Drone;
import com.castro.drones.enums.DroneModel;
import com.castro.drones.enums.Status;
import com.castro.drones.exception.DronException;
import com.castro.drones.exception.RegisterException;
import com.castro.drones.exception.ValidationException;
import com.castro.drones.repository.DroneRepository;
import com.castro.drones.response.DroneResponse;

@Service
public class DroneService {
	
	Drone drone = null;
	DroneResponse response = null;

	@Autowired
	DroneRepository droneRepository;
	
	public DroneResponse checkBatteryStatus(long idDrone) {
				
		int droneBattery = 0;
		drone = getDroneByID(idDrone);
		droneBattery = drone.getBatteryCapacity();
	
		if(droneBattery <25) {
			throw new DronException("Insufficient battery: " + droneBattery +"%");
		}else {
			response = new DroneResponse("device with enough battery: " + droneBattery +"%", drone);
		}	
		return response;
	}

	public DroneResponse createDrone(Drone droneData) throws Exception {

		drone = validationDrone(droneData);

		if (droneRepository.findAll().size() == 10) {
			throw new RegisterException("the fleet is already fully registered");
		} else if (droneRepository.findBySerialNumber(drone.getSerialNumber()).isPresent()) {
			throw new RegisterException("the device is already been registered");
		} else {
			response = new DroneResponse("device registered successfully", drone);
			droneRepository.save(drone);
		}
		return response;
	}
	
	public Drone validationDrone(Drone drone) {
		
		boolean serialNum = drone.getSerialNumber().trim().equals(StringUtils.EMPTY);
		boolean model = drone.getDronModel().trim().equals(StringUtils.EMPTY);

		String dModel = null;
		String serialN = "";

		if (serialNum) {
			throw new ValidationException("Serial Number can not be empty");
		} else if (drone.getSerialNumber().trim().length() > 100) {
			throw new ValidationException("the serial number exceeds the number of allowed digits");
		} else {
			serialN = drone.getSerialNumber().trim();
		}

		if (model) {
			throw new ValidationException("Dron Model can not be empty");
		} else {
			for (DroneModel dronModel : DroneModel.values()) {
				if (dronModel.toString().equals(drone.getDronModel().trim().toUpperCase())) {
					dModel = dronModel.toString();
				}
			}
		}
		
		if(dModel == null) {
			throw new ValidationException("that model does not exist for this type of device");
		}
		
		drone = new Drone(serialN, dModel);
		return drone;
	}	
	
	public List<Drone> findAllDrones(){
		
		List<Drone> dronesList = null;
		dronesList=droneRepository.findAll();
		
		if (dronesList.size() == 0) {
			throw new DronException("no device has been registered yet");	
		}
		
		return dronesList;
	}
	
	public List<Drone> getAvailableDrones() {
		
		List<Drone> dronesList = null;
				
		dronesList=droneRepository.findByIdleDron();

		if (dronesList.size() ==0) {
			throw new DronException("no devices available");
		}

		return dronesList;
	}
	
	public Drone getDronReadyToUse() {
		
		List<Drone> dronesList = getAvailableDrones();
		List<Drone> listDronesReady = new ArrayList<Drone>();
		
		for(Drone dronN: dronesList) {
			if (dronN.getBatteryCapacity() >25) {
				listDronesReady.add(dronN);
			}
		}
		
		if(listDronesReady.size() ==0) {
			throw new DronException("no devices available to use");
		}
		return listDronesReady.get(0);
	}
	
	public Drone getDroneByID(long idDron) {
		Optional<Drone> searchDron = droneRepository.findById(idDron);

		if (searchDron.isPresent()) {
			drone = searchDron.get();
		} else {
			throw new DronException("wrong device number, the device number is not found in the database");
		}
		return drone;
	}
	
	public DroneResponse confirmReturning(long idDron) {
		drone = getDroneByID(idDron);
		String nextStatus = Status.IDLE.toString();
		if (drone.getStatus().equals(Status.RETURNING.toString())) {
			drone.setStatus(nextStatus);
			droneRepository.updateDron(nextStatus, drone.getIdDron());
			response = new DroneResponse("dron landed successfully", drone);
		}else {
			throw new DronException("this operation cannot be perfomered, because the drone status is: " + drone.getStatus());
		}
		return response;
	}
	
	public DroneResponse rechargeBattery(long idDrone) {
		drone = getDroneByID(idDrone);
		drone.setBatteryCapacity(100);
		if (drone.getStatus().equalsIgnoreCase(Status.IDLE.toString())
				|| drone.getStatus().equalsIgnoreCase(Status.LOADING.toString())
				|| drone.getStatus().equalsIgnoreCase(Status.LOADED.toString())) {
			droneRepository.updateBattery(100, drone.getIdDron());
			response = new DroneResponse("battery recharged successfully", drone);
		}else {
			throw new DronException("can not recharge battery, because drone status is: " + drone.getStatus()+".Drone must be landed");
		}

		return response;
	}
	
	

}
