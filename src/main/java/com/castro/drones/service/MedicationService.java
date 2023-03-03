package com.castro.drones.service;

import java.util.List;
import java.util.Optional;

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
public class MedicationService{
	
	Medication medication = null;
	
	@Autowired
	MedicationRepository medicationRepository;
	
	public MedicationResponse createMedication(Medication medicationData) {
		
		medication = validateMedication(medicationData);
		MedicationResponse response = null;
		if (medicationRepository.findByCode(medication.getCode()).isPresent()) {
			throw new RegisterException("this medication is already registered in the database");
		} else {
			response = new MedicationResponse("medication registered successfully", medication);
			medicationRepository.save(medication);
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
				throw new ValidationException("this character is not allowed: " + medication.getName().charAt(i));
			}		
		}
		
		for (int i = 0 ; i< medication.getCode().trim().length(); i++) {
			if(! Character.isLetter(medication.getCode().charAt(i)) && ! (medication.getCode().charAt(i)== '-') && ! (medication.getCode().charAt(i)== '_')) {
				throw new ValidationException("this character is not allowed: " + medication.getCode().charAt(i));
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
		
		if (medication.getWeight() <=0) {
			throw new ValidationException("incorrect weight");
		} else if (medication.getWeight() > 500) {
			throw new ValidationException("this medication cannot be delivered by drone");
		}else {
			weight = medication.getWeight();
		}
		
		if(imageControl) {
			throw new ValidationException("image can not be empty");
		}else if (!StringUtils.right(medication.getImage().trim().toLowerCase(), 4).equals(".jpg") && !StringUtils.right(medication.getImage().trim().toLowerCase(), 4).equals(".png")) {
			throw new ValidationException("formats supported: .jpg y .png");
		}else {
			image = medication.getImage().trim();
		}
		
		medication = new Medication(name, weight, code, image);
		
		return medication;
	}
	
	public List<Medication> findAllMedication() {

		List<Medication> medicationList = null;
		medicationList = medicationRepository.findAll();

		if (medicationList.size() == 0) {
			throw new MedicationException("no medication has been registered yet");
		}

		return medicationList;
	}

	public Medication findMedicationById(long id) {

		Optional<Medication> searchMedication = medicationRepository.findById(id);
		if (searchMedication.isPresent()) {
			medication = searchMedication.get();
		} else {
			throw new MedicationException("this medication does not exist in the database");
		}
		return medication;
	}

}
