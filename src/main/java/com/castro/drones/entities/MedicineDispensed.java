package com.castro.drones.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Data @ToString
@Entity
@Table
public class MedicineDispensed implements Serializable {
	
	private static final long serialVerisionUID =1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idMedicineDispensed;
	
	@ManyToOne
	@JoinColumn(name = "idMedication", nullable = true, updatable = false)
	private Medication medication;
	
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "idShipping", nullable = true, updatable = false)
	@JsonBackReference
	private Shipping shipping;
	
	public MedicineDispensed(Medication medication, Shipping shipping) {
		super();
		this.medication = medication;
		this.shipping = shipping;
	}
	

}
