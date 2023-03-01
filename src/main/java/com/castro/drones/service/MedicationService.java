package com.castro.drones.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castro.drones.entities.Medication;
import com.castro.drones.exception.MedicationException;
import com.castro.drones.exception.RegisterException;
import com.castro.drones.exception.ValidationException;
import com.castro.drones.repository.MedicationRepository;
import com.castro.drones.response.MedicationResponse;

@Service
public class MedicationService {
	
	Medication medication = null;
	
	@Autowired
	MedicationRepository medicationRepository;
	
	public MedicationResponse createMedication(Medication medication) {
		
		medication = validateMedication(medication);
		MedicationResponse response = null;
		if (medicationRepository.findByCode(medication.getCode()).isPresent()) {
			throw new RegisterException("esa medicacion ya se encuentra en nuestras bases");
		} else {
			response = new MedicationResponse("medication registered successfully", medication);
		}
		
		return response;
	}
	
	public Medication validateMedication(Medication medication) {
		
		boolean nameControl= medication.getName().trim().equals(StringUtils.EMPTY);
		boolean codeControl = medication.getCode().trim().equals(StringUtils.EMPTY);
		boolean imageControl =medication.getImage().trim().equals(StringUtils.EMPTY);
		
		String name="";
		String code="";
		int weight = 0;
		String image = "";
		
		for (int i = 0 ; i <medication.getName().trim().length(); i++) {
			if( ! Character.isLetter(medication.getName().charAt(i)) && ! (medication.getName().charAt(i)== '-') && ! (medication.getName().charAt(i)== '_')) {
				throw new ValidationException("no se admite el caracter " + medication.getName().charAt(i) + " para el nombre");
			}		
		}
		
		for (int i = 0 ; i< medication.getCode().trim().length(); i++) {
			if(! Character.isLetter(medication.getCode().charAt(i)) && ! (medication.getCode().charAt(i)== '-') && ! (medication.getCode().charAt(i)== '_')) {
				throw new ValidationException("no se admite el caracter " + medication.getCode().charAt(i) + " para el codigo");
			}		
		}
		
		if(nameControl) {
			throw new ValidationException("medication's name can not be empty");
		}else {
			name= medication.getName().trim();
		}
		
		if(codeControl) {
			throw new ValidationException("medication's code can not be empty");
		}else {
			code = medication.getCode().trim().toUpperCase();
		}
		
		if (medication.getWeight() <0) {
			throw new ValidationException("peso incorrecto");
		} else if (medication.getWeight() > 500) {
			throw new ValidationException("este medicamento no se puede entregar via dron delivery");
		}else {
			weight = medication.getWeight();
		}
		
		if(imageControl) {
			throw new ValidationException("image can not be empty");
		}else if (!StringUtils.right(medication.getImage().trim().toLowerCase(), 4).equals(".jpg") && !StringUtils.right(medication.getImage().trim().toLowerCase(), 4).equals(".png")) {
			throw new ValidationException("solo se admite formatos .jpg y .png");
		}else {
			image = medication.getImage().trim();
		}
		
		medication = new Medication(name, weight, code, image);
		
		return medication;
	}
	
	
	public List<Medication> findAllMedication(){
		
		List<Medication> medicationList =null;
		medicationList = medicationRepository.findAll();
		
		if(medicationList.size()==0) {
			throw new MedicationException("no hay medicamentos registrados aun");
		}
		
		return medicationList;
	}

}
