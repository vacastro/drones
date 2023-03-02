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

import com.castro.drones.entities.Drone;
import com.castro.drones.response.DroneResponse;
import com.castro.drones.service.DroneService;

@RestController
@RequestMapping("/DRON-APP")
public class DroneController {
	
	@Autowired
	DroneService droneService;
	
	@PostMapping (value = "/drone-register")
	@ResponseStatus(HttpStatus.CREATED)
	public DroneResponse newDrone (@RequestBody Drone drone) throws Exception{
		return droneService.createDrone(drone);
	}
	
	@GetMapping("/all-drones")
	public List<Drone> findDrones(){
		return droneService.findAllDrones();
	}
	
	@GetMapping("/available-drones")
	public List<Drone> findDronesIdle(){
		return droneService.getAvailableDrones();
	}
	
	@GetMapping("/battery-status/{idDrone}")
	public DroneResponse checkBattery(@PathVariable("idDrone") long idDrone){
		return droneService.checkBatteryStatus(idDrone);
	}
	
	@GetMapping("/search-dron/{idDrone}")
	public Drone searchDrone(@PathVariable("idDrone") long idDrone){
		return droneService.getDroneByID(idDrone);
	}
	
	@PostMapping (value = "/confirm-returning")
	public DroneResponse confirmReturning(@RequestBody Drone drone) throws Exception{
		return droneService.confirmReturning(drone.getIdDron());
	}
	
	@PostMapping (value = "/recharge-battery")
	public DroneResponse rechargeBattery(@RequestBody Drone drone) throws Exception{
		return droneService.rechargeBattery(drone.getIdDron());
	}
	
	

}
