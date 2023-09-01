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
	private static final long serialVerisionUID =1L;
	
	@Column
	private Date date;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ShippingStatus shippingStatus;

	public ItineraryPK() {
		super();
		this.date = new Date();
		this.shippingStatus = ShippingStatus.ORDERED;
	}
	
	
	
	
	
}
