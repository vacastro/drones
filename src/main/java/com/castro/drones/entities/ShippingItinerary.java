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
public class ShippingItinerary {
	
	//TODO chequear la primary key compuesta
	@EmbeddedId
	private ShippingItineraryPK shippingItineraryPK;
	
	@ManyToOne
	@JoinColumn(name = "idShipping", nullable = false, updatable = false)
	private Shipping shipping;
	

	public ShippingItinerary(Shipping shipping) {
		super();
		this.shipping = shipping;
		this.shippingItineraryPK = new ShippingItineraryPK();
	}
	
	

}
