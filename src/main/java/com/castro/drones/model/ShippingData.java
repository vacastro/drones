package com.castro.drones.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ShippingData {
	
	private int idUser;	
	private int invoice;
	private String adress;

}
