package com.castro.drones.entities;

import java.io.Serializable;
import java.util.Date;

import com.castro.drones.enums.ShippingStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter @Setter @ToString
public class ItineraryPK implements Serializable {
	
	//TODO chequear que la primary key compuesta este bien hecha
	@Column
	private Date date;
	
	@Column
	private String shippingStatus;

	public ItineraryPK() {
		super();
		this.date = new Date();
		this.shippingStatus = ShippingStatus.ORDERED.toString();
	}
	
	
	
	
	
}
