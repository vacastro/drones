package com.castro.drones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Data @ToString
@Entity
@Table
public class MedicineDispensed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idMedicineDispensed;
	
	@ManyToOne
	@JoinColumn(name = "idMedication", nullable = true, updatable = false)
	private Medication medication;
	
	@ManyToOne
	@JoinColumn(name = "idShipping", nullable = false, updatable = false)
	private Shipping shipping;
	
	public MedicineDispensed(Medication medication, Shipping shipping) {
		super();
		this.medication = medication;
		this.shipping = shipping;
	}
	

}
