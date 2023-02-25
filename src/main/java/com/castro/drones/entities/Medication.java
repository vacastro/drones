package com.castro.drones.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Data @ToString
public class Medication {

	private String name;
	private int weight;
	private String code;
	private String image;
	
	
	
}
