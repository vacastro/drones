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
public class Itinerary {
	
	//TODO chequear la primary key compuesta
	@EmbeddedId
	private ItineraryPK pk;
	
	@ManyToOne
	@JoinColumn(name = "idShipping", nullable = false, updatable = false)
	private Shipping shipping;
	

	public Itinerary(Shipping shipping) {
		super();
		this.shipping = shipping;
		this.pk = new ItineraryPK();
	}
	
	

}
