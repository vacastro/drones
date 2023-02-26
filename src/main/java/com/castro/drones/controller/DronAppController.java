package com.castro.drones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.castro.drones.entities.Dron;
import com.castro.drones.entities.Medication;
import com.castro.drones.model.DronData;
import com.castro.drones.response.DronResponse;
import com.castro.drones.response.MedicationResponse;
import com.castro.drones.service.DronService;
import com.castro.drones.service.MedicationService;


@RestController
@RequestMapping("/DRON-APP")
public class DronAppController {
	
	@Autowired
	DronService dronService;
	
	@Autowired
	MedicationService medicationService;
	
	@PostMapping (value = "/dron-register")
	@ResponseStatus(HttpStatus.CREATED)
	public DronResponse newDron (@RequestBody DronData dronData) throws Exception{
		return dronService.createDron(dronData);
	}
	
	@GetMapping("/all-drones")
	public List<Dron> findDrones(){
		return dronService.findAllDrones();
	}
	
	@GetMapping("/available-drones")
	public List<Dron> findDronesIdle(){
		return dronService.getIdleDrones();
	}
	
	@PostMapping (value = "/medication-register")
	@ResponseStatus(HttpStatus.CREATED)
	public MedicationResponse newMedication (@RequestBody Medication medication) throws Exception{
		return medicationService.createMedication(medication);
	}
	
	@GetMapping("/all-medication")
	public List<Medication> findMedication(){
		return medicationService.findAllMedication();
	}

}
