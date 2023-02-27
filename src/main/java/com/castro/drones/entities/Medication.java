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
public class Medication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idMedication;

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private int weight;
	
	@Column(nullable=false)
	private String code;
	
	@Column(nullable=false)
	private String image;
	

	public Medication(String name, int weight, String code, String image) {
		super();
		this.name = name;
		this.weight = weight;
		this.code = code;
		this.image = image;
	}
	
	
	
	
	
}
