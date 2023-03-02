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

import com.castro.drones.entities.Medication;
import com.castro.drones.response.MedicationResponse;
import com.castro.drones.service.MedicationService;

@RestController
@RequestMapping("/DRON-APP")
public class MedicationController {
	
	@Autowired
	MedicationService medicationService;
	
	@PostMapping (value = "/medication-register")
	@ResponseStatus(HttpStatus.CREATED)
	public MedicationResponse newMedication (@RequestBody Medication medication) throws Exception{
		return medicationService.createMedication(medication);
	}
	
	@GetMapping("/all-medication")
	public List<Medication> findMedication(){
		return medicationService.findAllMedication();
	}
	
	@GetMapping("/search-medication/{idMedication}")
	public Medication searchMedication(@PathVariable("idMedication") long idMedication){
		return medicationService.findMedicationById(idMedication);
	}

}
